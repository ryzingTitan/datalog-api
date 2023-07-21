package com.ryzingtitan.datalogapi.presentation.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
@Configuration
class SecurityConfiguration {
    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .authorizeExchange { exchange ->
                exchange.pathMatchers(HttpMethod.GET, "/api/sessions/**")
                    .permitAll()
                    .pathMatchers(HttpMethod.OPTIONS, "/api/sessions/**")
                    .permitAll()
                    .pathMatchers(HttpMethod.GET, "/actuator/health")
                    .permitAll()
                    .pathMatchers("/**")
                    .denyAll()
            }
            .cors { it.disable() }
            .csrf { it.disable() }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }

        return http
            .build()
    }
}
