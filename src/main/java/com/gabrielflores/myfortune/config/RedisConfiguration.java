package com.gabrielflores.myfortune.config;

import com.gabrielflores.myfortune.dto.ErrorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories(value = "com.gabrielflores.myfortune.redis.repository")
public class RedisConfiguration {

    private final RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        return new StringRedisTemplate(redisConnectionFactory());
    }

    @Bean
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate() {
        return new ReactiveStringRedisTemplate(redisConnectionFactory());
    }

    @Bean
    public RedisTemplate<String, ErrorDto> redisTemplateErro() {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<ErrorDto> valueSerializer = new Jackson2JsonRedisSerializer<>(ErrorDto.class);
        RedisTemplate<String, ErrorDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setDefaultSerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public ReactiveRedisTemplate<String, ErrorDto> reactiveRedisTemplateErro() {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<ErrorDto> valueSerializer = new Jackson2JsonRedisSerializer<>(ErrorDto.class);
        RedisSerializationContextBuilder<String, ErrorDto> contextBuilder = RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, ErrorDto> context = contextBuilder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(redisConnectionFactory(), context);
    }
}
