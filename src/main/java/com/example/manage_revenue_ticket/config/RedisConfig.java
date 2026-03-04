package com.example.manage_revenue_ticket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {
  // ==============================
  // Cache Manager
  // ==============================
  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory factory) {

    RedisCacheConfiguration config =
      RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(
          RedisSerializationContext.SerializationPair
            .fromSerializer(new StringRedisSerializer())
        )
        .serializeValuesWith(
          RedisSerializationContext.SerializationPair
            .fromSerializer(new GenericJackson2JsonRedisSerializer())
        );

    return RedisCacheManager.builder(factory)
      .cacheDefaults(config)
      .build();
  }

  // ==============================
  // 🔥 ADD THIS: RedisTemplate
  // ==============================
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {

    // 🔥 Tạo ObjectMapper custom
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    GenericJackson2JsonRedisSerializer serializer =
      new GenericJackson2JsonRedisSerializer(objectMapper);

    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);

    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());

    template.setValueSerializer(serializer);
    template.setHashValueSerializer(serializer);

    template.afterPropertiesSet();
    return template;
  }
}
