package com.ryzingtitan.datalogapi.domain.session.services

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.data.datalog.entities.DataEntity
import com.ryzingtitan.datalogapi.data.datalog.entities.DatalogEntity
import com.ryzingtitan.datalogapi.data.datalog.entities.TrackInfoEntity
import com.ryzingtitan.datalogapi.data.datalog.entities.UserEntity
import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
import com.ryzingtitan.datalogapi.domain.session.configuration.ColumnConfiguration
import com.ryzingtitan.datalogapi.domain.session.dtos.FileUpload
import com.ryzingtitan.datalogapi.domain.session.dtos.FileUploadMetadata
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import java.util.*

@ExperimentalCoroutinesApi
class FileParsingServiceTests {
    @Nested
    inner class Parse {
        @Test
        fun `reads the input data and creates data log records`() = runTest {
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
        fileParsingService = FileParsingService(
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

    private val sessionId = UUID.randomUUID()

    private val fileUploadMetadata = FileUploadMetadata(
        fileName = "testFile.txt",
        sessionId = sessionId,
        trackInfo = TrackInfo(
            name = trackName,
            latitude = trackLatitude,
            longitude = trackLongitude,
        ),
        user = User(
            email = userEmail,
            firstName = userFirstName,
            lastName = userLastName,
        ),
    )

    private val columnConfiguration = ColumnConfiguration(
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

    private val datalog = DatalogEntity(
        sessionId = sessionId,
        epochMilliseconds = Instant.now().toEpochMilli(),
        data = DataEntity(
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
        ),
        trackInfo = TrackInfoEntity(
            name = "Test Track",
            latitude = 42.4086,
            longitude = -86.1374,
        ),
        user = UserEntity(
            email = "test@test.com",
            firstName = "test",
            lastName = "tester",
        ),
    )

    companion object FileParsingServiceTestConstants {
        const val trackName = "Test Track"
        const val trackLatitude = 42.4086
        const val trackLongitude = -86.1374

        const val userEmail = "test@test.com"
        const val userFirstName = "test"
        const val userLastName = "tester"
    }
}
