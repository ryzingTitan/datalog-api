package com.ryzingtitan.datalogapi.domain.track.services

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import com.ryzingtitan.datalogapi.data.track.entities.TrackEntity
import com.ryzingtitan.datalogapi.data.track.repositories.TrackRepository
import com.ryzingtitan.datalogapi.domain.track.dtos.Track
import com.ryzingtitan.datalogapi.domain.track.exceptions.TrackAlreadyExistsException
import com.ryzingtitan.datalogapi.domain.track.exceptions.TrackDoesNotExistException
import com.ryzingtitan.datalogapi.domain.uuid.UuidGenerator
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import java.util.UUID

@ExperimentalCoroutinesApi
class TrackServiceTests {
    @Nested
    inner class Create {
        @Test
        fun `creates a new track`() =
            runTest {
                val expectedTrackId = UUID.randomUUID()
                whenever(mockUuidGenerator.generate()).thenReturn(expectedTrackId)
                whenever(mockTrackRepository.findByName(FIRST_TRACK_NAME)).thenReturn(emptyFlow())

                val trackId = trackService.create(firstTrack.copy(id = null))

                assertEquals(expectedTrackId, trackId)
                assertEquals(1, appender.list.size)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals("Created track named $FIRST_TRACK_NAME", appender.list[0].message)

                verify(mockTrackRepository, times(1)).findByName(FIRST_TRACK_NAME)
                verify(mockUuidGenerator, times(1)).generate()
                verify(mockTrackRepository, times(1)).save(firstTrackEntity.copy(trackId = expectedTrackId))
            }

        @Test
        fun `does not create a duplicate track`() =
            runTest {
                val expectedTrackId = UUID.randomUUID()
                whenever(mockUuidGenerator.generate()).thenReturn(expectedTrackId)
                whenever(mockTrackRepository.findByName(FIRST_TRACK_NAME)).thenReturn(flowOf(firstTrackEntity))

                val exception =
                    assertThrows<TrackAlreadyExistsException> {
                        trackService.create(firstTrack.copy(id = null))
                    }

                assertEquals("A track already exists named $FIRST_TRACK_NAME", exception.message)
                assertEquals(1, appender.list.size)
                assertEquals(Level.ERROR, appender.list[0].level)
                assertEquals("A track already exists named $FIRST_TRACK_NAME", appender.list[0].message)

                verify(mockTrackRepository, times(1)).findByName(FIRST_TRACK_NAME)
                verify(mockUuidGenerator, never()).generate()
                verify(mockTrackRepository, never()).save(any())
            }
    }

    @Nested
    inner class Update {
        @Test
        fun `updates an existing track`() =
            runTest {
                whenever(mockTrackRepository.findByName(SECOND_TRACK_NAME)).thenReturn(flowOf(secondTrackEntity))
                whenever(mockTrackRepository.deleteByTrackId(secondTrackId)).thenReturn(flowOf(secondTrackEntity))

                trackService.update(secondTrack)

                assertEquals(1, appender.list.size)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals("Updated track named $SECOND_TRACK_NAME", appender.list[0].message)

                verify(mockTrackRepository, times(1)).findByName(SECOND_TRACK_NAME)
                verify(mockTrackRepository, times(1)).deleteByTrackId(secondTrackId)
                verify(mockTrackRepository, times(1)).save(secondTrackEntity)
            }

        @Test
        fun `does not update a track that does not exist`() =
            runTest {
                whenever(mockTrackRepository.findByName(SECOND_TRACK_NAME)).thenReturn(emptyFlow())

                val exception =
                    assertThrows<TrackDoesNotExistException> {
                        trackService.update(secondTrack)
                    }

                assertEquals("A track named $SECOND_TRACK_NAME does not exist", exception.message)
                assertEquals(1, appender.list.size)
                assertEquals(Level.ERROR, appender.list[0].level)
                assertEquals("A track named $SECOND_TRACK_NAME does not exist", appender.list[0].message)

                verify(mockTrackRepository, times(1)).findByName(SECOND_TRACK_NAME)
                verify(mockTrackRepository, never()).deleteByTrackId(any())
                verify(mockTrackRepository, never()).save(any())
            }
    }

    @Nested
    inner class GetAll {
        @Test
        fun `returns all tracks`() =
            runTest {
                whenever(mockTrackRepository.findAll()).thenReturn(flowOf(firstTrackEntity, secondTrackEntity))

                val tracks = trackService.getAll()

                assertEquals(listOf(firstTrack, secondTrack), tracks.toList())
                assertEquals(1, appender.list.size)
                assertEquals(Level.INFO, appender.list[0].level)
                assertEquals("Retrieving all tracks", appender.list[0].message)
            }
    }

    @BeforeEach
    fun setup() {
        trackService = TrackService(mockTrackRepository, mockUuidGenerator)

        logger = LoggerFactory.getLogger(TrackService::class.java) as Logger
        appender = ListAppender()
        appender.context = LoggerContext()
        logger.addAppender(appender)
        appender.start()
    }

    private lateinit var trackService: TrackService
    private lateinit var logger: Logger
    private lateinit var appender: ListAppender<ILoggingEvent>

    private val mockTrackRepository = mock<TrackRepository>()
    private val mockUuidGenerator = mock<UuidGenerator>()

    private val firstTrackId = UUID.randomUUID()
    private val secondTrackId = UUID.randomUUID()

    private val firstTrackEntity =
        TrackEntity(
            trackId = firstTrackId,
            name = FIRST_TRACK_NAME,
            latitude = FIRST_TRACK_LATITUDE,
            longitude = FIRST_TRACK_LONGITUDE,
        )

    private val firstTrack =
        Track(
            id = firstTrackId,
            name = FIRST_TRACK_NAME,
            latitude = FIRST_TRACK_LATITUDE,
            longitude = FIRST_TRACK_LONGITUDE,
        )

    private val secondTrackEntity =
        TrackEntity(
            trackId = secondTrackId,
            name = SECOND_TRACK_NAME,
            latitude = SECOND_TRACK_LATITUDE,
            longitude = SECOND_TRACK_LONGITUDE,
        )

    private val secondTrack =
        Track(
            id = secondTrackId,
            name = SECOND_TRACK_NAME,
            latitude = SECOND_TRACK_LATITUDE,
            longitude = SECOND_TRACK_LONGITUDE,
        )

    companion object TrackServiceTestConstants {
        const val FIRST_TRACK_NAME = "Test Track 1"
        const val FIRST_TRACK_LATITUDE = 12.0
        const val FIRST_TRACK_LONGITUDE = 14.0

        const val SECOND_TRACK_NAME = "Test Track 2"
        const val SECOND_TRACK_LATITUDE = 30.0
        const val SECOND_TRACK_LONGITUDE = 33.0
    }
}
