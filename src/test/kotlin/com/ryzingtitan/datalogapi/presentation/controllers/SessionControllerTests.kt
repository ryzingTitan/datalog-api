package com.ryzingtitan.datalogapi.presentation.controllers

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.domain.sessions.dtos.FileUpload
import com.ryzingtitan.datalogapi.domain.sessions.dtos.Session
import com.ryzingtitan.datalogapi.domain.sessions.services.SessionService
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import org.springframework.core.io.FileSystemResource
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import org.springframework.web.reactive.function.BodyInserters
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant

class SessionControllerTests {
    @Nested
    inner class GetAllSessionsByUser {
        @Test
        fun `returns 'OK' status with all user sessions`() =
            runTest {
                whenever(mockSessionService.getAllByUser("test@test.com"))
                    .thenReturn(flowOf(firstSession, secondSession))

                webTestClient
                    .get()
                    .uri("/api/sessions?userEmail=test@test.com")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk
                    .expectBodyList<Session>()
                    .contains(firstSession, secondSession)

                assertEquals(1, appender.list.size)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals("Retrieving all sessions for user: test@test.com", appender.list[0].message)

                verify(mockSessionService, times(1)).getAllByUser("test@test.com")
            }
    }

    @Nested
    inner class CreateSession {
        @Test
        fun `returns 'CREATED' status and creates new session`() =
            runTest {
                val sessionId = 1
                whenever(mockSessionService.create(any<FileUpload>())).thenReturn(sessionId)

                Files.createDirectory(Path.of("testFiles"))
                Files.write(Path.of("testFiles", "testFile.txt"), listOf(""))

                val multipartBodyBuilder = MultipartBodyBuilder()
                multipartBodyBuilder.part("userEmail", USER_EMAIL)
                multipartBodyBuilder.part("userLastName", USER_LAST_NAME)
                multipartBodyBuilder.part("userFirstName", USER_FIRST_NAME)
                multipartBodyBuilder.part("trackId", 2)
                multipartBodyBuilder.part("uploadFile", FileSystemResource("testFiles/testFile.txt"))
                val multiPartData = multipartBodyBuilder.build()

                webTestClient
                    .post()
                    .uri("/api/sessions")
                    .body(BodyInserters.fromMultipartData(multiPartData))
                    .exchange()
                    .expectStatus()
                    .isCreated
                    .expectHeader()
                    .location("/api/sessions/$sessionId")

                verify(mockSessionService, times(1)).create(any<FileUpload>())
            }
    }

    @Nested
    inner class UpdateSession {
        @Test
        fun `returns 'OK' status and updates session data`() =
            runTest {
                Files.createDirectory(Path.of("testFiles"))
                Files.write(Path.of("testFiles", "testFile.txt"), listOf(""))

                val multipartBodyBuilder = MultipartBodyBuilder()
                multipartBodyBuilder.part("userEmail", USER_EMAIL)
                multipartBodyBuilder.part("userLastName", USER_LAST_NAME)
                multipartBodyBuilder.part("userFirstName", USER_FIRST_NAME)
                multipartBodyBuilder.part("trackId", 2)
                multipartBodyBuilder.part("uploadFile", FileSystemResource("testFiles/testFile.txt"))
                val multiPartData = multipartBodyBuilder.build()

                val sessionId = 5

                webTestClient
                    .put()
                    .uri("/api/sessions/$sessionId")
                    .body(BodyInserters.fromMultipartData(multiPartData))
                    .exchange()
                    .expectStatus()
                    .isOk

                verify(mockSessionService, times(1)).update(any<FileUpload>())
            }
    }

    @BeforeEach
    fun setup() {
        val sessionController = SessionController(mockSessionService)
        webTestClient = WebTestClient.bindToController(sessionController).build()

        logger = LoggerFactory.getLogger(SessionController::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()

        Files.deleteIfExists(Path.of("testFiles", "testFile.txt"))
        Files.deleteIfExists(Path.of("testFiles"))
    }

    private lateinit var webTestClient: WebTestClient
    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val mockSessionService = mock<SessionService>()

    private val firstSession =
        Session(
            id = 1,
            startTime = Instant.now(),
            endTime = Instant.now().plusSeconds(60),
            trackName = TRACK_NAME,
            trackLatitude = TRACK_LATITUDE,
            trackLongitude = TRACK_LONGITUDE,
        )

    private val secondSession =
        Session(
            id = 2,
            startTime = Instant.now().plusSeconds(120),
            endTime = Instant.now().plusSeconds(180),
            trackName = TRACK_NAME,
            trackLatitude = TRACK_LATITUDE,
            trackLongitude = TRACK_LONGITUDE,
        )

    companion object FileUploadControllerTestConstants {
        const val USER_EMAIL = "test@test.com"
        const val USER_FIRST_NAME = "test"
        const val USER_LAST_NAME = "tester"
        const val TRACK_NAME = "Test Track"
        const val TRACK_LATITUDE = 12.0
        const val TRACK_LONGITUDE = 14.0
    }
}
