package com.Chitti.AiVoiceMail.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
//@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
//        objectMapper.activateDefaultTyping(
//                new DefaultBaseTypeLimitingValidator(),
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );
        objectMapper.registerModule(new JavaTimeModule()); // Support for LocalDateTime
        objectMapper.findAndRegisterModules();
        PolymorphicTypeValidator validator = LaissezFaireSubTypeValidator.instance;
        objectMapper.activateDefaultTyping(
                validator,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        RedisSerializationContext.SerializationPair<String> keySerializer =
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());

        GenericJackson2JsonRedisSerializer valueSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);


        // Default cache configuration
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(keySerializer)
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer))
                .disableCachingNullValues();

        // Custom configurations for specific caches
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // TTL for applicationConfigs: 24 hours
        cacheConfigurations.put("applicationConfigs", defaultCacheConfig.entryTtl(Duration.ofHours(24)));

        // TTL for chatHistory: 5 minutes
        cacheConfigurations.put("chatHistory", defaultCacheConfig.entryTtl(Duration.ofMinutes(5)));
        // TTL for userCustomizations: 60 minutes
        cacheConfigurations.put("userCustomizations", defaultCacheConfig.entryTtl(Duration.ofMinutes(60)));
        // TTL for userDetails: 24 hours
        cacheConfigurations.put("userDetails", defaultCacheConfig.entryTtl(Duration.ofHours(24)));
        // TTL for audioMetadata: 10 minutes
        cacheConfigurations.put("audioMetadata", defaultCacheConfig.entryTtl(Duration.ofMinutes(10)));

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
