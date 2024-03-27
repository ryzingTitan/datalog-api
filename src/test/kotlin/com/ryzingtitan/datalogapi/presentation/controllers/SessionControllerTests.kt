package com.ryzingtitan.datalogapi.presentation.controllers

import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
import com.ryzingtitan.datalogapi.domain.session.dtos.FileUpload
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.core.io.FileSystemResource
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt
import org.springframework.web.reactive.function.BodyInserters
import java.nio.file.Files
import java.nio.file.Path
import java.util.UUID

@ExperimentalCoroutinesApi
class SessionControllerTests : CommonControllerTests() {
    @Nested
    inner class CreateSession {
        @Test
        fun `returns 'CREATED' status and creates new session`() =
            runTest {
                val sessionId = UUID.randomUUID()
                whenever(mockSessionService.create(any<FileUpload>())).thenReturn(sessionId)

                Files.createDirectory(Path.of("testFiles"))
                Files.write(Path.of("testFiles", "testFile.txt"), listOf(""))

                val multipartBodyBuilder = MultipartBodyBuilder()
                multipartBodyBuilder.part("userEmail", user.email)
                multipartBodyBuilder.part("userLastName", user.lastName)
                multipartBodyBuilder.part("userFirstName", user.firstName)
                multipartBodyBuilder.part("trackName", trackInfo.name)
                multipartBodyBuilder.part("trackLatitude", trackInfo.latitude)
                multipartBodyBuilder.part("trackLongitude", trackInfo.longitude)
                multipartBodyBuilder.part("uploadFile", FileSystemResource("testFiles/testFile.txt"))
                val multiPartData = multipartBodyBuilder.build()

                webTestClient
                    .mutateWith(mockJwt())
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
                multipartBodyBuilder.part("userEmail", user.email)
                multipartBodyBuilder.part("userLastName", user.lastName)
                multipartBodyBuilder.part("userFirstName", user.firstName)
                multipartBodyBuilder.part("trackName", trackInfo.name)
                multipartBodyBuilder.part("trackLatitude", trackInfo.latitude)
                multipartBodyBuilder.part("trackLongitude", trackInfo.longitude)
                multipartBodyBuilder.part("uploadFile", FileSystemResource("testFiles/testFile.txt"))
                val multiPartData = multipartBodyBuilder.build()

                val sessionId = UUID.randomUUID()

                webTestClient
                    .mutateWith(mockJwt())
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
        Files.deleteIfExists(Path.of("testFiles", "testFile.txt"))
        Files.deleteIfExists(Path.of("testFiles"))
    }

    private val user =
        User(
            firstName = USER_FIRST_NAME,
            lastName = USER_LAST_NAME,
            email = USER_EMAIL,
        )
    private val trackInfo =
        TrackInfo(
            name = TRACK_NAME,
            latitude = TRACK_LATITUDE,
            longitude = TRACK_LONGITUDE,
        )

    companion object FileUploadControllerTestConstants {
        const val TRACK_NAME = "Test Track"
        const val TRACK_LATITUDE = 42.4086
        const val TRACK_LONGITUDE = -86.1374

        const val USER_EMAIL = "test@test.com"
        const val USER_FIRST_NAME = "test"
        const val USER_LAST_NAME = "tester"
    }
}
