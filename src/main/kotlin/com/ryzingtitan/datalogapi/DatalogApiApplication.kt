package com.ryzingtitan.datalogapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication(exclude = [ReactiveUserDetailsServiceAutoConfiguration::class])
@ConfigurationPropertiesScan
@EnableCaching
class DatalogApiApplication

fun main(args: Array<String>) {
    runApplication<DatalogApiApplication>(arrayOf(args).contentDeepToString())
}
