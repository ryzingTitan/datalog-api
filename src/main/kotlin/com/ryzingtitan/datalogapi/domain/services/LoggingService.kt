package com.ryzingtitan.datalogapi.domain.services

import com.ryzingtitan.datalogapi.domain.configuration.SpringApplicationProperties
import net.logstash.logback.marker.Markers
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class LoggingService(
    private val springApplicationProperties: SpringApplicationProperties
) {
    fun info(logger: Logger, userId: Int, message: String) {
        logger.info(
            Markers.append("userId", userId)
                .and(Markers.append("applicationName", springApplicationProperties.name)),
            message
        )
    }

    fun error(logger: Logger, userId: Int, message: String, exception: Exception) {
        logger.error(
            Markers.append("userId", userId)
                .and(Markers.append("applicationName", springApplicationProperties.name)),
            message,
            exception
        )
    }
}
