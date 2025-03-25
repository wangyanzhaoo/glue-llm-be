package com.rune.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author avalon
 * @date 22/3/31 16:33
 * @description Redis缓存配置
 */
@Configuration(proxyBeanMethods = false)
@EnableCaching
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class RedisConfig implements CachingConfigurer {

    /**
     * 设置 Redis 默认缓存配置，序列化方式为 JSON，并设置缓存过期时间为 2 小时
     */
    @Bean
    public static RedisCacheConfiguration redisCacheConfiguration() {
        Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .entryTtl(Duration.ofHours(2));
    }

    /**
     * 自定义 RedisTemplate，设置 key 和 hashKey 使用 String 序列化，value 使用 JSON 序列化
     */
    @Bean
    public static RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        // 序列化 Key 和 HashKey 使用 String 序列化器
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        // 序列化 Value 和 HashValue 使用 JSON 序列化器
        Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /**
     * 配置 Spring Cache 的 CacheManager，默认过期时间为 6 小时，并针对 key "config" 设置永不过期
     */
    @Bean
    public static CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisSerializationContext.SerializationPair<String> keyPair =
                RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer);
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        // 默认缓存配置：6 小时过期，不缓存 null 值
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(6))
                .disableCachingNullValues();

        // 针对 key "config" 的配置：永不过期，指定 key 和 value 的序列化器
        RedisCacheConfiguration configCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofNanos(-1))
                .disableCachingNullValues()
                .serializeKeysWith(keyPair)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("config", configCacheConfig);

        // 全局 RedisCacheConfiguration
        RedisCacheConfiguration redisCacheConfiguration = defaultConfig
                .serializeKeysWith(keyPair)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
