package com.rune.annotation;

import com.rune.enums.LogTypeEnum;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author avalon
 * @date 22/8/5 17:00
 * @description 删除日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@OperationLogging(type = LogTypeEnum.DELETE)
public @interface DeleteLog {

    /**
     * 日志信息
     *
     * @return 日志描述信息
     */
    @AliasFor(annotation = OperationLogging.class)
    String msg();
}
