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
import com.ryzingtitan.datalogapi.data.datalog.repositories.DatalogRepository
import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
import com.ryzingtitan.datalogapi.domain.session.dtos.FileUpload
import com.ryzingtitan.datalogapi.domain.session.dtos.FileUploadMetadata
import com.ryzingtitan.datalogapi.domain.session.exceptions.SessionAlreadyExistsException
import com.ryzingtitan.datalogapi.domain.session.exceptions.SessionDoesNotExistException
import com.ryzingtitan.datalogapi.domain.sessionmetadata.services.SessionMetadataService
import com.ryzingtitan.datalogapi.domain.uuid.UuidGenerator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import java.time.Instant
import java.util.UUID

@ExperimentalCoroutinesApi
class SessionServiceTests {
    @Nested
    inner class Create {
        @Test
        fun `creates a new session`() =
            runTest {
                whenever(mockFileParsingService.parse(any<FileUpload>())).thenReturn(listOf(datalog))
                whenever(mockSessionMetadataService.getExistingSessionId(USER_EMAIL, datalog.epochMilliseconds))
                    .thenReturn(null)

                val newSessionId = UUID.randomUUID()
                whenever(mockUuidGenerator.generate()).thenReturn(newSessionId)

                val updatedDatalog = datalog.copy(sessionId = newSessionId)
                whenever(mockDatalogRepository.saveAll(listOf(updatedDatalog))).thenReturn(flowOf(updatedDatalog))

                val sessionId = sessionService.create(FileUpload(flowOf(dataBuffer), fileUploadMetadata))

                assertEquals(newSessionId, sessionId)
                assertEquals(1, appender.list.size)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals("Session $newSessionId created", appender.list[0].message)

                verify(mockFileParsingService, times(1)).parse(any<FileUpload>())
                verify(mockSessionMetadataService, times(1))
                    .getExistingSessionId(USER_EMAIL, datalog.epochMilliseconds)
                verify(mockUuidGenerator, times(1)).generate()
                verify(mockDatalogRepository, times(1)).saveAll(listOf(datalog.copy(sessionId = newSessionId)))
            }

        @Test
        fun `does not create duplicate sessions for a user`() =
            runTest {
                whenever(mockFileParsingService.parse(any<FileUpload>())).thenReturn(listOf(datalog))
                whenever(mockSessionMetadataService.getExistingSessionId(USER_EMAIL, datalog.epochMilliseconds))
                    .thenReturn(UUID.randomUUID())

                val exception =
                    assertThrows<SessionAlreadyExistsException> {
                        sessionService.create(FileUpload(flowOf(dataBuffer), fileUploadMetadata))
                    }

                assertEquals("A session already exists for this user and timestamp", exception.message)
                assertEquals(1, appender.list.size)
                assertEquals(Level.ERROR, appender.list[0].level)
                assertEquals("A session already exists for this user and timestamp", appender.list[0].message)

                verify(mockFileParsingService, times(1)).parse(any<FileUpload>())
                verify(mockSessionMetadataService, times(1))
                    .getExistingSessionId(USER_EMAIL, datalog.epochMilliseconds)
                verify(mockUuidGenerator, never()).generate()
            }
    }

    @Nested
    inner class Update {
        @Test
        fun `updates an existing session`() =
            runTest {
                val currentSessionId = UUID.randomUUID()

                whenever(mockDatalogRepository.findAllBySessionId(currentSessionId))
                    .thenReturn(flowOf(datalog.copy(sessionId = currentSessionId)))
                whenever(mockDatalogRepository.deleteBySessionId(currentSessionId))
                    .thenReturn(flowOf(datalog.copy(sessionId = currentSessionId)))
                whenever(mockFileParsingService.parse(any<FileUpload>()))
                    .thenReturn(listOf(datalog.copy(sessionId = currentSessionId)))
                whenever(mockDatalogRepository.saveAll(listOf(datalog.copy(sessionId = currentSessionId))))
                    .thenReturn(flowOf(datalog.copy(sessionId = currentSessionId)))

                sessionService.update(
                    FileUpload(
                        flowOf(dataBuffer),
                        fileUploadMetadata.copy(sessionId = currentSessionId),
                    ),
                )

                assertEquals(1, appender.list.size)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals("Session $currentSessionId updated", appender.list[0].message)

                verify(mockDatalogRepository, times(1)).findAllBySessionId(currentSessionId)
                verify(mockDatalogRepository, times(1)).deleteBySessionId(currentSessionId)
                verify(mockFileParsingService, times(1)).parse(any<FileUpload>())
                verify(mockDatalogRepository, times(1))
                    .saveAll(listOf(datalog.copy(sessionId = currentSessionId)))
            }

        @Test
        fun `does not update a session that does not exist`() =
            runTest {
                val currentSessionId = UUID.randomUUID()

                whenever(mockDatalogRepository.findAllBySessionId(currentSessionId)).thenReturn(emptyFlow())

                val exception =
                    assertThrows<SessionDoesNotExistException> {
                        sessionService
                            .update(
                                FileUpload(
                                    flowOf(dataBuffer),
                                    fileUploadMetadata.copy(sessionId = currentSessionId),
                                ),
                            )
                    }

                assertEquals("Session id $currentSessionId does not exist", exception.message)
                assertEquals(1, appender.list.size)
                assertEquals(Level.ERROR, appender.list[0].level)
                assertEquals("Session id $currentSessionId does not exist", appender.list[0].message)

                verify(mockDatalogRepository, times(1)).findAllBySessionId(currentSessionId)
                verify(mockDatalogRepository, never()).deleteBySessionId(any())
                verify(mockFileParsingService, never()).parse(any())
                verify(mockDatalogRepository, never()).saveAll(any<List<DatalogEntity>>())
            }
    }

    @BeforeEach
    fun setup() {
        sessionService =
            SessionService(
                mockFileParsingService,
                mockDatalogRepository,
                mockUuidGenerator,
                mockSessionMetadataService,
            )

        logger = LoggerFactory.getLogger(SessionService::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()
    }

    private lateinit var sessionService: SessionService
    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val mockFileParsingService = mock<FileParsingService>()
    private val mockDatalogRepository = mock<DatalogRepository>()
    private val mockUuidGenerator = mock<UuidGenerator>()
    private val mockSessionMetadataService = mock<SessionMetadataService>()

    private val dataBufferFactory = DefaultDataBufferFactory()
    private val dataBuffer = dataBufferFactory.wrap("header row 1\ndata row 1\n".toByteArray())

    private val fileUploadMetadata =
        FileUploadMetadata(
            fileName = "testFile.txt",
            sessionId = null,
            trackInfo =
                TrackInfo(
                    name = TRACK_NAME,
                    latitude = TRACK_LATITUDE,
                    longitude = TRACK_LONGITUDE,
                ),
            user =
                User(
                    email = USER_EMAIL,
                    firstName = USER_FIRST_NAME,
                    lastName = USER_LAST_NAME,
                ),
        )

    private val datalog =
        DatalogEntity(
            sessionId = null,
            epochMilliseconds = Instant.now().toEpochMilli(),
            data =
                DataEntity(
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
            trackInfo =
                TrackInfoEntity(
                    name = "Test Track",
                    latitude = 42.4086,
                    longitude = -86.1374,
                ),
            user =
                UserEntity(
                    email = "test@test.com",
                    firstName = "test",
                    lastName = "tester",
                ),
        )

    companion object SessionServiceTestConstants {
        const val TRACK_NAME = "Test Track"
        const val TRACK_LATITUDE = 42.4086
        const val TRACK_LONGITUDE = -86.1374

        const val USER_EMAIL = "test@test.com"
        const val USER_FIRST_NAME = "test"
        const val USER_LAST_NAME = "tester"
    }
}
