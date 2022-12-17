package com.ryzingtitan.datalogapi.domain.configuration

import lombok.Generated
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@Generated
@ConstructorBinding
@ConfigurationProperties(prefix = "spring.application")
data class SpringApplicationProperties(
    val name: String
)
