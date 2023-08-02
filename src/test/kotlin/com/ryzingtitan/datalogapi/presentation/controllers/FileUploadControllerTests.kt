package com.ryzingtitan.datalogapi.presentation.controllers

import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
import com.ryzingtitan.datalogapi.domain.fileupload.dtos.FileUpload
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
import java.util.*

@ExperimentalCoroutinesApi
class FileUploadControllerTests : CommonControllerTests() {
    @Nested
    inner class Upload {
        @Test
        fun `returns 'OK' and creates correct file upload`() = runTest {
            whenever(mockUuidGenerator.generate()).thenReturn(UUID.randomUUID())

            Files.createDirectory(Path.of("testFiles"))
            Files.write(Path.of("testFiles", "testFile.txt"), listOf(""))

            val multipartBodyBuilder = MultipartBodyBuilder()
            multipartBodyBuilder.part("user", user)
            multipartBodyBuilder.part("trackInfo", trackInfo)
            multipartBodyBuilder.part("uploadFile", FileSystemResource("testFiles/testFile.txt"))
            val multiPartData = multipartBodyBuilder.build()

            webTestClient
                .mutateWith(mockJwt())
                .put()
                .uri("/api/sessions")
                .body(BodyInserters.fromMultipartData(multiPartData))
                .exchange()
                .expectStatus()
                .isOk

            verify(mockUuidGenerator, times(1)).generate()
            verify(mockFileParsingService, times(1)).parse(any<FileUpload>())
        }
    }

    private val user = User(
        firstName = userFirstName,
        lastName = userLastName,
        email = userEmail,
    )
    private val trackInfo = TrackInfo(
        name = trackName,
        latitude = trackLatitude,
        longitude = trackLongitude,
    )

    companion object FileUploadControllerTestConstants {
        const val trackName = "Test Track"
        const val trackLatitude = 42.4086
        const val trackLongitude = -86.1374

        const val userEmail = "test@test.com"
        const val userFirstName = "test"
        const val userLastName = "tester"
    }
}
