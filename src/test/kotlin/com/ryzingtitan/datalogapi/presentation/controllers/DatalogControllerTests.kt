package com.ryzingtitan.datalogapi.presentation.controllers

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.domain.datalog.dtos.Data
import com.ryzingtitan.datalogapi.domain.datalog.dtos.Datalog
import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt
import org.springframework.test.web.reactive.server.expectBodyList
import java.time.Instant
import java.util.UUID

class DatalogControllerTests : CommonControllerTests() {
    @Nested
    inner class GetDatalogsBySessionId {
        @Test
        fun `returns 'OK' status with session data that matches the request parameter`() {
            whenever(mockDatalogService.getAllBySessionId(sessionId))
                .thenReturn(flowOf(firstDatalog, secondDatalog))

            webTestClient
                .mutateWith(mockJwt())
                .get()
                .uri("/api/sessions/$sessionId/datalogs")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList<Datalog>()
                .contains(firstDatalog, secondDatalog)

            assertEquals(1, appender.list.size)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals("Retrieving datalogs for session id: $sessionId", appender.list[0].message)

            verify(mockDatalogService, times(1)).getAllBySessionId(sessionId)
        }
    }

    @BeforeEach
    fun setup() {
        reset(mockDatalogService)

        logger = LoggerFactory.getLogger(DatalogController::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()
    }

    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val sessionId = UUID.randomUUID()

    private val firstDatalog =
        Datalog(
            sessionId = sessionId,
            timestamp = Instant.now(),
            data =
                Data(
                    longitude = -86.14162,
                    latitude = 42.406800000000004,
                    altitude = 188.4f,
                    intakeAirTemperature = 130,
                    boostPressure = 15.6f,
                    coolantTemperature = 150,
                    engineRpm = 5000,
                    speed = 85,
                    throttlePosition = 75.6f,
                    airFuelRatio = 14.7f,
                ),
            trackInfo = TrackInfo("", 0.0, 0.0),
            user = User("", "", ""),
        )

    private val secondDatalog =
        Datalog(
            sessionId = sessionId,
            timestamp = Instant.now(),
            data =
                Data(
                    longitude = 86.14162,
                    latitude = -42.406800000000004,
                    altitude = 188.0f,
                    intakeAirTemperature = 135,
                    boostPressure = 15.0f,
                    coolantTemperature = 165,
                    engineRpm = 5500,
                    speed = 80,
                    throttlePosition = 75.0f,
                    airFuelRatio = 15.9f,
                ),
            trackInfo = TrackInfo("", 0.0, 0.0),
            user = User("", "", ""),
        )
}
