package com.example.demo.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.babyfish.jimmer.meta.ImmutableProp
import org.babyfish.jimmer.meta.ImmutableType
import org.babyfish.jimmer.sql.cache.Cache
import org.babyfish.jimmer.sql.cache.chain.ChainCacheBuilder
import org.babyfish.jimmer.sql.cache.redis.spring.RedisValueBinder
import org.babyfish.jimmer.sql.kt.cache.KCacheFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import java.time.Duration

@Configuration
class JimmerConfiguration {
//    @Bean
//    fun cacheFactory(
//        connectionFactory: RedisConnectionFactory,
//        objectMapper: ObjectMapper
//    ): KCacheFactory {
//        return object : KCacheFactory {
//
//            override fun createObjectCache(type: ImmutableType): Cache<*, *>? =
//                ChainCacheBuilder<Any, Any>()
//                    .add(
//                        RedisValueBinder
//                            .forObject<Any, Any>(type)
//                            .redis(connectionFactory)
//                            .objectMapper(objectMapper)
//                            .duration(Duration.ofHours(24))
//                            .build()
//                    )
//                    .build()
//
//            override fun createAssociatedIdCache(prop: ImmutableProp): Cache<*, *>? =
//                ChainCacheBuilder<Any, Any>()
//                    .add(
//                        RedisValueBinder
//                            .forProp<Any, Any>(prop)
//                            .redis(connectionFactory)
//                            .objectMapper(objectMapper)
//                            .duration(Duration.ofHours(10))
//                            .build()
//                    )
//                    .build()
//
//            override fun createAssociatedIdListCache(prop: ImmutableProp): Cache<*, List<*>>? =
//                ChainCacheBuilder<Any, List<*>>()
//                    .add(
//                        RedisValueBinder
//                            .forProp<Any, List<*>>(prop)
//                            .redis(connectionFactory)
//                            .objectMapper(objectMapper)
//                            .duration(Duration.ofHours(10))
//                            .build()
//                    )
//                    .build()
//
//            override fun createResolverCache(prop: ImmutableProp): Cache<*, *> =
//                ChainCacheBuilder<Any, Any>()
//                    .add(
//                        RedisValueBinder
//                            .forProp<Any, Any>(prop)
//                            .redis(connectionFactory)
//                            .objectMapper(objectMapper)
//                            .duration(Duration.ofHours(24))
//                            .build()
//                    )
//                    .build()
//        }
//    }
}