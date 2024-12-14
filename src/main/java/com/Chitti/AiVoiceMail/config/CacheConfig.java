package com.Chitti.AiVoiceMail.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisSerializationContext.SerializationPair<String> serializer =
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());

        // Default cache configuration
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(serializer)
                .serializeValuesWith(serializer)
                .disableCachingNullValues();

        // Custom configurations for specific caches
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // TTL for applicationConfigs: 24 hours
        cacheConfigurations.put("applicationConfigs", defaultCacheConfig.entryTtl(Duration.ofHours(24)));

        // TTL for chatHistory: 5 minutes
        cacheConfigurations.put("chatHistory", defaultCacheConfig.entryTtl(Duration.ofMinutes(5)));

        // Build RedisCacheManager with default and custom configurations
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig) // Set the default cache configuration
                .withInitialCacheConfigurations(cacheConfigurations) // Add the custom cache configurations
                .build();
    }

    @Bean
    public SimpleKeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

}
