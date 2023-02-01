package com.ryzingtitan.datalogapi.presentation.controllers

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.domain.datalogrecord.dtos.DatalogRecord
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import java.time.Instant
import java.util.*

class DatalogControllerTests : CommonControllerTests() {
    @Nested
    inner class GetDatalogsBySessionId {
        @Test
        fun `returns 'OK' status with session data that matches the request parameter`() {
            whenever(mockDatalogRecordService.getAllBySessionId(sessionId))
                .thenReturn(flowOf(firstDatalogRecord, secondDatalogRecord))

            webTestClient.get()
                .uri("/api/sessions/$sessionId/datalogs")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList(DatalogRecord::class.java)
                .contains(firstDatalogRecord, secondDatalogRecord)

            assertEquals(1, appender.list.size)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals("Retrieving datalog records for session id: $sessionId", appender.list[0].message)
        }
    }

    @BeforeEach
    fun setup() {
        reset(mockDatalogRecordService)

        logger = LoggerFactory.getLogger(DatalogController::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()
    }

    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val sessionId = UUID.randomUUID()

    private val firstDatalogRecord = DatalogRecord(
        sessionId = sessionId,
        timestamp = Instant.now(),
        longitude = -86.14162,
        latitude = 42.406800000000004,
        altitude = 188.4f,
        intakeAirTemperature = 130,
        boostPressure = 15.6f,
        coolantTemperature = 150,
        engineRpm = 5000,
        speed = 85,
        throttlePosition = 75.6f,
    )

    private val secondDatalogRecord = DatalogRecord(
        sessionId = sessionId,
        timestamp = Instant.now(),
        longitude = 86.14162,
        latitude = -42.406800000000004,
        altitude = 188.0f,
        intakeAirTemperature = 135,
        boostPressure = 15.0f,
        coolantTemperature = 165,
        engineRpm = 5500,
        speed = 80,
        throttlePosition = 75.0f,
    )
}
