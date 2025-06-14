package com.example.demo.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.babyfish.jimmer.jackson.ImmutableModule
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@EnableCaching
@Configuration
class RedisConfiguration {
    @Bean
    fun cacheManager(
        redisConnectionFactory: RedisConnectionFactory,
        redisSerializer: RedisSerializer<Any?>
    ): CacheManager {
        val redisCacheManager = RedisCacheManager(
            RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
            this.getRedisCacheConfigurationWithTtl(3600, redisSerializer),
            this.getRedisCacheConfigurationMap(redisSerializer)
        )
        redisCacheManager.isTransactionAware = true
        return redisCacheManager
    }

    private fun getRedisCacheConfigurationMap(redisSerializer: RedisSerializer<Any?>): MutableMap<String?, RedisCacheConfiguration?> {
        val redisCacheConfigurationMap: MutableMap<String?, RedisCacheConfiguration?> =
            HashMap(16)
        redisCacheConfigurationMap.put("tree", this.getRedisCacheConfigurationWithTtl(1800, redisSerializer))
        return redisCacheConfigurationMap
    }

    private fun getRedisCacheConfigurationWithTtl(
        seconds: Int,
        redisSerializer: RedisSerializer<Any?>
    ): RedisCacheConfiguration {
        var redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
            RedisSerializationContext
                .SerializationPair
                .fromSerializer<Any?>(redisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds.toLong()))

        return redisCacheConfiguration
    }

    @Bean
    fun redisTemplate(
        redisConnectionFactory: RedisConnectionFactory?,
        redisSerializer: RedisSerializer<Any?>
    ): RedisTemplate<String?, Any?> {
        val redisTemplate = RedisTemplate<String?, Any?>()
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        redisTemplate.setKeySerializer(StringRedisSerializer())
        redisTemplate.setHashKeySerializer(StringRedisSerializer())
        redisTemplate.setValueSerializer(redisSerializer)
        redisTemplate.setHashValueSerializer(redisSerializer)
        redisTemplate.setEnableTransactionSupport(false)

        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }

    /**
     * 自定义redis序列化的机制,重新定义一个ObjectMapper.防止和MVC的冲突
     * https://juejin.im/post/5e869d426fb9a03c6148c97e
     */
    @Bean
    fun redisSerializer(): RedisSerializer<Any?> {
        val objectMapper: ObjectMapper = JsonMapper.builder().build()
        // 反序列化时候遇到不匹配的属性并不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        // 序列化时候遇到空对象不抛出异常
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        // 反序列化的时候如果是无效子类型,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
        // 不使用默认的dateTime进行序列化,
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
        // 使用JSR310提供的序列化类,里面包含了大量的JDK8时间序列化类
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerModule(ImmutableModule())
        objectMapper.registerModule(KotlinModule.Builder().build())
        // 启用反序列化所需的类型信息,在属性中添加@class
        objectMapper.activateDefaultTyping(
            LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
            JsonTypeInfo.As.PROPERTY
        )
        // 配置null值的序列化器
        GenericJackson2JsonRedisSerializer.registerNullValueSerializer(objectMapper, null)
        return GenericJackson2JsonRedisSerializer(objectMapper)
    }


    @Bean
    fun stringRedisTemplate(redisConnectionFactory: RedisConnectionFactory): StringRedisTemplate {
        val redisTemplate = StringRedisTemplate(redisConnectionFactory)
        redisTemplate.setEnableTransactionSupport(false)
        return redisTemplate
    }
}