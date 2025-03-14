package com.rune.utils.jsonSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.rune.annotation.DictKey;
import com.rune.utils.RedisUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author sedate
 * @date 2024/3/28 16:58
 * @description 字典注解序列化实现
 */
@Component
public class DictSerializer extends JsonSerializer<String> {

    private static RedisUtils redisUtils;

    private final String dictPrefix = "dict::";

    @Autowired
    public void setRedisUtils(RedisUtils redisUtils) {
        DictSerializer.redisUtils = redisUtils;
    }

    /**
     * 根据注解中的字典值和字段内容字典项值获取字典中的数据
     *
     * @param s                  当前字段数据
     * @param jsonGenerator      /
     * @param serializerProvider /
     * @throws IOException
     */
    @SneakyThrows
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String currentName = jsonGenerator.getOutputContext().getCurrentName();
        DictKey dictKey = getAnnotationValue(jsonGenerator);
        if (dictKey == null || StringUtils.isBlank(dictKey.value())) {
            jsonGenerator.writeString("");
        } else {
            String virtual = null;
            String value = dictKey.value();
            Object result = redisUtils.get(dictPrefix + value);
            if (StringUtils.isNotBlank(s)) {
                // s 不为空代表只显示字典项
                ArrayList<LinkedHashMap<String, String>> linkedHashMaps = (ArrayList<LinkedHashMap<String, String>>) result;
                for (LinkedHashMap<String, String> linkedHashMap : linkedHashMaps) {
                    String string = linkedHashMap.get("value");
                    if (s.equals(string)) {
                        // 判断显示整个对象还是单独的文字
                        if (dictKey.insideObject()) {
                            result = new HashMap<String, String>() {{
                                put("label", linkedHashMap.get("text"));
                                put("value", linkedHashMap.get("value"));
                            }};
                        } else {
                            result = linkedHashMap.get("value");
                            virtual = linkedHashMap.get("text");
                        }
                    }
                }
            }
            jsonGenerator.writeString(result == null ? "" : result.toString());
            // 显示单独的文字 需要将字典项值和字典项内容都返回
            if (StringUtils.isNotBlank(virtual)) {
                jsonGenerator.writeStringField(currentName + "Text", virtual);
            }
        }
    }

    /**
     * 获取当前字段注解
     *
     * @param jsonGenerator /
     * @return 注解内容
     */
    private DictKey getAnnotationValue(JsonGenerator jsonGenerator) throws Exception {
        JsonStreamContext outputContext = jsonGenerator.getOutputContext();
        Object currentValue = outputContext.getCurrentValue();
        Class<?> aClass = currentValue.getClass();
        String currentName = outputContext.getCurrentName();
        return aClass.getDeclaredField(currentName).getAnnotation(DictKey.class);
    }
}
