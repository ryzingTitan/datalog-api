package com.ryzingtitan.datalogapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [ReactiveUserDetailsServiceAutoConfiguration::class])
@ConfigurationPropertiesScan
class DatalogApiApplication

fun main(args: Array<String>) {
    runApplication<DatalogApiApplication>(arrayOf(args).contentDeepToString())
}
