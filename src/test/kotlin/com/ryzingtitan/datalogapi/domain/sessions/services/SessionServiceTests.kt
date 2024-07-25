package com.ryzingtitan.datalogapi.domain.sessions.services

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.data.datalogs.entities.DatalogEntity
import com.ryzingtitan.datalogapi.data.datalogs.repositories.DatalogRepository
import com.ryzingtitan.datalogapi.data.sessions.entities.SessionEntity
import com.ryzingtitan.datalogapi.data.sessions.repositories.SessionRepository
import com.ryzingtitan.datalogapi.data.tracks.entities.TrackEntity
import com.ryzingtitan.datalogapi.data.tracks.repositories.TrackRepository
import com.ryzingtitan.datalogapi.domain.sessions.dtos.FileUpload
import com.ryzingtitan.datalogapi.domain.sessions.dtos.FileUploadMetadata
import com.ryzingtitan.datalogapi.domain.sessions.dtos.Session
import com.ryzingtitan.datalogapi.domain.sessions.exceptions.SessionAlreadyExistsException
import com.ryzingtitan.datalogapi.domain.sessions.exceptions.SessionDoesNotExistException
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
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

class SessionServiceTests {
    @Nested
    inner class GetAllByUser {
        @Test
        fun `returns all sessions for user`() =
            runTest {
                whenever(mockSessionRepository.findAllByUserEmail(userEmail))
                    .thenReturn(flowOf(firstSessionEntity, secondSessionEntity))
                whenever(mockTrackRepository.findById(trackId)).thenReturn(trackEntity)

                val sessions = sessionService.getAllByUser(userEmail)

                assertEquals(listOf(firstSession, secondSession), sessions.toList())
            }
    }

    @Nested
    inner class Create {
        @Test
        fun `creates a new session`() =
            runTest {
                whenever(mockFileParsingService.parse(any<FileUpload>())).thenReturn(listOf(datalog))
                whenever(
                    mockSessionRepository.findByUserEmailAndStartTimeAndEndTime(
                        userEmail,
                        datalog.timestamp,
                        datalog.timestamp,
                    ),
                ).thenReturn(null)
                whenever(mockSessionRepository.save(firstSessionEntity.copy(id = null))).thenReturn(firstSessionEntity)

                val updatedDatalog = datalog.copy(sessionId = firstSessionEntity.id)
                whenever(mockDatalogRepository.saveAll(listOf(updatedDatalog))).thenReturn(flowOf(updatedDatalog))

                val sessionId = sessionService.create(FileUpload(flowOf(dataBuffer), fileUploadMetadata))

                assertEquals(firstSessionEntity.id, sessionId)
                assertEquals(1, appender.list.size)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals("Session $sessionId created", appender.list[0].message)

                verify(mockFileParsingService, times(1)).parse(any<FileUpload>())
                verify(mockSessionRepository, times(1)).findByUserEmailAndStartTimeAndEndTime(
                    userEmail,
                    datalog.timestamp,
                    datalog.timestamp,
                )
                verify(mockSessionRepository, times(1)).save(firstSessionEntity.copy(id = null))
                verify(mockDatalogRepository, times(1)).saveAll(listOf(datalog.copy(sessionId = sessionId)))
            }

        @Test
        fun `does not create duplicate sessions for a user`() =
            runTest {
                whenever(mockFileParsingService.parse(any<FileUpload>())).thenReturn(listOf(datalog))
                whenever(
                    mockSessionRepository.findByUserEmailAndStartTimeAndEndTime(
                        userEmail,
                        datalog.timestamp,
                        datalog.timestamp,
                    ),
                ).thenReturn(firstSessionEntity)

                val exception =
                    assertThrows<SessionAlreadyExistsException> {
                        sessionService.create(FileUpload(flowOf(dataBuffer), fileUploadMetadata))
                    }

                assertEquals("A session already exists for this user and timestamp", exception.message)
                assertEquals(1, appender.list.size)
                assertEquals(Level.ERROR, appender.list[0].level)
                assertEquals("A session already exists for this user and timestamp", appender.list[0].message)

                verify(mockFileParsingService, times(1)).parse(any<FileUpload>())
                verify(mockSessionRepository, times(1)).findByUserEmailAndStartTimeAndEndTime(
                    userEmail,
                    datalog.timestamp,
                    datalog.timestamp,
                )
                verify(mockSessionRepository, never()).save(any())
                verify(mockDatalogRepository, never()).saveAll(any<List<DatalogEntity>>())
            }
    }

    @Nested
    inner class Update {
        @Test
        fun `updates an existing session`() =
            runTest {
                val currentSessionId = 1

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
                val currentSessionId = 1

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
                mockSessionRepository,
                mockTrackRepository,
                mockFileParsingService,
                mockDatalogRepository,
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

    private val mockTrackRepository = mock<TrackRepository>()
    private val mockSessionRepository = mock<SessionRepository>()
    private val mockFileParsingService = mock<FileParsingService>()
    private val mockDatalogRepository = mock<DatalogRepository>()

    private val userEmail = "test@test.com"
    private val trackId = 5
    private val timestamp = Instant.now()
    private val trackName = "Test Track"
    private val trackLatitude = 12.0
    private val trackLongitude = 14.0
    private val dataBufferFactory = DefaultDataBufferFactory()
    private val dataBuffer = dataBufferFactory.wrap("header row 1\ndata row 1\n".toByteArray())

    private val firstSessionEntity =
        SessionEntity(
            id = 1,
            userEmail = userEmail,
            userFirstName = USER_FIRST_NAME,
            userLastName = USER_LAST_NAME,
            startTime = timestamp,
            endTime = timestamp,
            trackId = trackId,
        )

    private val secondSessionEntity =
        SessionEntity(
            id = 2,
            userEmail = userEmail,
            userFirstName = USER_FIRST_NAME,
            userLastName = USER_LAST_NAME,
            startTime = timestamp,
            endTime = timestamp,
            trackId = trackId,
        )

    private val trackEntity =
        TrackEntity(
            id = 5,
            name = trackName,
            latitude = trackLatitude,
            longitude = trackLongitude,
        )

    private val firstSession =
        Session(
            id = firstSessionEntity.id!!,
            startTime = firstSessionEntity.startTime,
            endTime = firstSessionEntity.endTime,
            trackName = trackName,
            trackLatitude = trackLatitude,
            trackLongitude = trackLongitude,
        )

    private val secondSession =
        Session(
            id = secondSessionEntity.id!!,
            startTime = secondSessionEntity.startTime,
            endTime = secondSessionEntity.endTime,
            trackName = trackName,
            trackLatitude = trackLatitude,
            trackLongitude = trackLongitude,
        )

    private val fileUploadMetadata =
        FileUploadMetadata(
            fileName = "testFile.txt",
            sessionId = null,
            trackId = trackId,
            userEmail = USER_EMAIL,
            userFirstName = USER_FIRST_NAME,
            userLastName = USER_LAST_NAME,
        )

    private val datalog =
        DatalogEntity(
            sessionId = 2,
            timestamp = timestamp,
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

    companion object SessionServiceTestConstants {
        const val USER_EMAIL = "test@test.com"
        const val USER_FIRST_NAME = "test"
        const val USER_LAST_NAME = "tester"
    }
}
