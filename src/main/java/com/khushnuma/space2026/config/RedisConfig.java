package com.khushnuma.space2026.config;

import com.khushnuma.space2026.dto.LaunchLibraryResponse;
import com.khushnuma.space2026.dto.LaunchpadLibraryResponse;
import com.khushnuma.space2026.dto.SpaceXLaunchResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory,
            ObjectMapper objectMapper) {

        // LaunchLibraryResponse serializer
        JacksonJsonRedisSerializer<LaunchLibraryResponse>
                launchSerializer =
                new JacksonJsonRedisSerializer<>(
                        objectMapper,
                        LaunchLibraryResponse.class
                );

        // LaunchpadLibraryResponse serializer
        JacksonJsonRedisSerializer<LaunchpadLibraryResponse>
                launchpadSerializer =
                new JacksonJsonRedisSerializer<>(
                        objectMapper,
                        LaunchpadLibraryResponse.class
                );

        // List<SpaceXLaunchResponse> serializer
        JavaType upcomingType =
                objectMapper.getTypeFactory()
                        .constructCollectionType(
                                List.class,
                                SpaceXLaunchResponse.class
                        );

        JacksonJsonRedisSerializer<List<SpaceXLaunchResponse>>
                upcomingSerializer =
                new JacksonJsonRedisSerializer<>(
                        objectMapper,
                        upcomingType
                );

        // Launches cache
        RedisCacheConfiguration launchConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(30))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(launchSerializer)
                        );

        // Launchpads cache
        RedisCacheConfiguration launchpadConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(30))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(launchpadSerializer)
                        );

        // Upcoming launches cache
        RedisCacheConfiguration upcomingConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(30))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(upcomingSerializer)
                        );

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .withCacheConfiguration(
                        "spacexLaunches",
                        launchConfig
                )
                .withCacheConfiguration(
                        "spacexLaunchpads",
                        launchpadConfig
                )
                .withCacheConfiguration(
                        "spacexUpcoming",
                        upcomingConfig
                )
                .build();
    }
}