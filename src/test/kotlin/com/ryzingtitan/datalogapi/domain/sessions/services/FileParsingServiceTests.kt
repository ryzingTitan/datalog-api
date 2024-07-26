package com.ryzingtitan.datalogapi.domain.sessions.services

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.data.datalogs.entities.DatalogEntity
import com.ryzingtitan.datalogapi.domain.sessions.configuration.ColumnConfiguration
import com.ryzingtitan.datalogapi.domain.sessions.dtos.FileUpload
import com.ryzingtitan.datalogapi.domain.sessions.dtos.FileUploadMetadata
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import java.time.Instant

class FileParsingServiceTests {
    @Nested
    inner class Parse {
        @Test
        fun `reads the input data and creates datalogs`() =
            runTest {
                whenever(
                    mockRowParsingService.parse(
                        "data row 1",
                        fileUploadMetadata,
                        columnConfiguration,
                    ),
                )
                    .thenReturn(datalog)

                val dataBufferFactory = DefaultDataBufferFactory()
                val dataBuffer = dataBufferFactory.wrap("header row 1\ndata row 1\n".toByteArray())

                val datalogs = fileParsingService.parse(FileUpload(flowOf(dataBuffer), fileUploadMetadata))

                verify(mockColumnConfigurationService, times(1)).create("header row 1")
                verify(mockRowParsingService, times(1)).parse(
                    "data row 1",
                    fileUploadMetadata,
                    columnConfiguration,
                )

                assertEquals(listOf(datalog), datalogs)
                assertEquals(2, appender.list.size)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals("Beginning to parse file: testFile.txt", appender.list[0].message)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals("File parsing completed for file: testFile.txt", appender.list[1].message)
            }
    }

    @BeforeEach
    fun setup() {
        fileParsingService =
            FileParsingService(
                mockRowParsingService,
                mockColumnConfigurationService,
            )

        whenever(mockColumnConfigurationService.create("header row 1")).thenReturn(columnConfiguration)

        logger = LoggerFactory.getLogger(FileParsingService::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()
    }

    private lateinit var fileParsingService: FileParsingService
    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val mockRowParsingService = mock<RowParsingService>()
    private val mockColumnConfigurationService = mock<ColumnConfigurationService>()

    private val fileUploadMetadata =
        FileUploadMetadata(
            fileName = "testFile.txt",
            sessionId = SESSION_ID,
            trackId = TRACK_ID,
            userEmail = USER_EMAIL,
            userFirstName = USER_FIRST_NAME,
            userLastName = USER_LAST_NAME,
        )

    private val columnConfiguration =
        ColumnConfiguration(
            deviceTime = 0,
            longitude = 1,
            latitude = 2,
            altitude = 3,
            coolantTemperature = 4,
            engineRpm = 5,
            intakeAirTemperature = 6,
            speed = 7,
            throttlePosition = 8,
            boostPressure = 9,
            airFuelRatio = 10,
        )

    private val datalog =
        DatalogEntity(
            sessionId = SESSION_ID,
            timestamp = Instant.now(),
            longitude = -86.14162,
            latitude = 42.406800000000004,
            altitude = 188.4f,
            intakeAirTemperature = 138,
            boostPressure = 16.5f,
            coolantTemperature = 155,
            engineRpm = 3500,
            speed = 79,
            throttlePosition = 83.2f,
            airFuelRatio = 17.5f,
        )

    companion object FileParsingServiceTestConstants {
        const val USER_EMAIL = "test@test.com"
        const val USER_FIRST_NAME = "test"
        const val USER_LAST_NAME = "tester"
        const val SESSION_ID = 1
        const val TRACK_ID = 3
    }
}
