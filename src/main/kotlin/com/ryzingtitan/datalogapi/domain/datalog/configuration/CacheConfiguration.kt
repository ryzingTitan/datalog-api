package com.ryzingtitan.datalogapi.domain.datalog.configuration

import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CacheConfiguration {
    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = CaffeineCacheManager()
        cacheManager.setAsyncCacheMode(true)
        return cacheManager
    }
}
