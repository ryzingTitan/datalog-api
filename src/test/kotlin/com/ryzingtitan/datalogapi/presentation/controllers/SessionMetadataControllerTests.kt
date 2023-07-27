package com.ryzingtitan.datalogapi.presentation.controllers

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.domain.sessionmetadata.dtos.SessionMetadata
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt
import java.time.Instant
import java.util.*

class SessionMetadataControllerTests : CommonControllerTests() {

    @Nested
    inner class GetSessionMetadataByUser {
        @Test
        fun `returns 'OK' status with session metadata for all user sessions`() {
            whenever(mockSessionMetadataService.getAllSessionMetadataByUser("test@test.com"))
                .thenReturn(flowOf(firstSessionMetadata, secondSessionMetadata))

            webTestClient
                .mutateWith(mockJwt())
                .get()
                .uri("/api/sessions/metadata?username=test@test.com")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk
                .expectBodyList(SessionMetadata::class.java)
                .contains(firstSessionMetadata, secondSessionMetadata)

            assertEquals(1, appender.list.size)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals("Retrieving metadata for all sessions for user: test@test.com", appender.list[0].message)
        }
    }

    @BeforeEach
    fun setup() {
        reset(mockSessionMetadataService)

        logger = LoggerFactory.getLogger(SessionMetadataController::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()
    }

    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val firstSessionMetadata = SessionMetadata(
        sessionId = UUID.randomUUID(),
        startTime = Instant.now(),
        endTime = Instant.now(),
    )

    private val secondSessionMetadata = SessionMetadata(
        sessionId = UUID.randomUUID(),
        startTime = Instant.now(),
        endTime = Instant.now(),
    )
}
