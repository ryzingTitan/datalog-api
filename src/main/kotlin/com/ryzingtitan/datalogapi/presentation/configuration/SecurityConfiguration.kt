package com.ryzingtitan.datalogapi.presentation.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
@Configuration
class SecurityConfiguration {
    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .authorizeExchange { exchange ->
                exchange.pathMatchers(HttpMethod.OPTIONS, "/api/**")
                    .permitAll()
                    .pathMatchers(HttpMethod.GET, "/api/sessions/**")
                    .authenticated()
                    .pathMatchers(HttpMethod.PUT, "/api/sessions/*")
                    .authenticated()
                    .pathMatchers(HttpMethod.POST, "/api/sessions")
                    .authenticated()
                    .pathMatchers(HttpMethod.GET, "/api/tracks")
                    .authenticated()
                    .pathMatchers(HttpMethod.POST, "/api/tracks")
                    .authenticated()
                    .pathMatchers(HttpMethod.PUT, "/api/tracks/*")
                    .authenticated()
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
            .oauth2ResourceServer { it.jwt { Customizer.withDefaults<JwtSpec>() } }

        return http
            .build()
    }
}
