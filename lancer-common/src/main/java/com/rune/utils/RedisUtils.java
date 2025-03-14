package com.rune.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author avalon
 * @date 22/3/31 15:52
 * @description Redis 工具
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUtils {

    private final RedisTemplate<Object, Object> redisTemplate;

    /**
     * 根据 key 查找
     *
     * @param key /
     * @return value
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 简单插入
     *
     * @param key   /
     * @param value /
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 插入 设置过期时间
     *
     * @param key   /
     * @param value /
     * @param time  /
     */
    public boolean set(String key, Object value, long time) {
        try {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 如果为空就set值，并返回1；如果存在(不为空)不进行操作，并返回0。
     *
     * @param key   /
     * @param value /
     * @param time  /
     */
    public boolean setIfAbsent(String key, Object value, long time) {
        try {
            redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 对指定的键值加一
     *
     * @param key /
     */
    public boolean increment(String key) {
        try {
            redisTemplate.opsForValue().increment(key);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 根据给定的前缀字符串从 Redis 中扫描并返回所有与该前缀匹配的键的集合。
     *
     * @param prefix 要匹配的键前缀字符串
     * @return 与前缀匹配的键的集合
     */
    public Set<Object> keyScanner(String prefix){
        Set<Object> keys;
        String pattern = prefix + "*";
        keys = redisTemplate.keys(pattern);
        return keys;
    }
}
