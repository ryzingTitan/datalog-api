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
        fun `creates a new track`() = runTest {
            val expectedTrackId = UUID.randomUUID()
            whenever(mockUuidGenerator.generate()).thenReturn(expectedTrackId)
            whenever(mockTrackRepository.findByName(firstTrackName)).thenReturn(emptyFlow())

            val trackId = trackService.create(firstTrack.copy(id = null))

            assertEquals(expectedTrackId, trackId)
            assertEquals(1, appender.list.size)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals("Created track named $firstTrackName", appender.list[0].message)

            verify(mockTrackRepository, times(1)).findByName(firstTrackName)
            verify(mockUuidGenerator, times(1)).generate()
            verify(mockTrackRepository, times(1)).save(firstTrackEntity.copy(trackId = expectedTrackId))
        }

        @Test
        fun `does not create a duplicate track`() = runTest {
            val expectedTrackId = UUID.randomUUID()
            whenever(mockUuidGenerator.generate()).thenReturn(expectedTrackId)
            whenever(mockTrackRepository.findByName(firstTrackName)).thenReturn(flowOf(firstTrackEntity))

            val exception = assertThrows<TrackAlreadyExistsException> {
                trackService.create(firstTrack.copy(id = null))
            }

            assertEquals("A track already exists named $firstTrackName", exception.message)
            assertEquals(1, appender.list.size)
            assertEquals(Level.ERROR, appender.list[0].level)
            assertEquals("A track already exists named $firstTrackName", appender.list[0].message)

            verify(mockTrackRepository, times(1)).findByName(firstTrackName)
            verify(mockUuidGenerator, never()).generate()
            verify(mockTrackRepository, never()).save(any())
        }
    }

    @Nested
    inner class Update {
        @Test
        fun `updates an existing track`() = runTest {
            whenever(mockTrackRepository.findByName(secondTrackName)).thenReturn(flowOf(secondTrackEntity))
            whenever(mockTrackRepository.deleteByTrackId(secondTrackId)).thenReturn(flowOf(secondTrackEntity))

            trackService.update(secondTrack)

            assertEquals(1, appender.list.size)
            assertEquals(Level.INFO, appender.list[0].level)
            assertEquals("Updated track named $secondTrackName", appender.list[0].message)

            verify(mockTrackRepository, times(1)).findByName(secondTrackName)
            verify(mockTrackRepository, times(1)).deleteByTrackId(secondTrackId)
            verify(mockTrackRepository, times(1)).save(secondTrackEntity)
        }

        @Test
        fun `does not update a track that does not exist`() = runTest {
            whenever(mockTrackRepository.findByName(secondTrackName)).thenReturn(emptyFlow())

            val exception = assertThrows<TrackDoesNotExistException> {
                trackService.update(secondTrack)
            }

            assertEquals("A track named $secondTrackName does not exist", exception.message)
            assertEquals(1, appender.list.size)
            assertEquals(Level.ERROR, appender.list[0].level)
            assertEquals("A track named $secondTrackName does not exist", appender.list[0].message)

            verify(mockTrackRepository, times(1)).findByName(secondTrackName)
            verify(mockTrackRepository, never()).deleteByTrackId(any())
            verify(mockTrackRepository, never()).save(any())
        }
    }

    @Nested
    inner class GetAll {
        @Test
        fun `returns all tracks`() = runTest {
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

    private val firstTrackEntity = TrackEntity(
        trackId = firstTrackId,
        name = firstTrackName,
        latitude = firstTrackLatitude,
        longitude = firstTrackLongitude,
    )

    private val firstTrack = Track(
        id = firstTrackId,
        name = firstTrackName,
        latitude = firstTrackLatitude,
        longitude = firstTrackLongitude,
    )

    private val secondTrackEntity = TrackEntity(
        trackId = secondTrackId,
        name = secondTrackName,
        latitude = secondTrackLatitude,
        longitude = secondTrackLongitude,
    )

    private val secondTrack = Track(
        id = secondTrackId,
        name = secondTrackName,
        latitude = secondTrackLatitude,
        longitude = secondTrackLongitude,
    )

    companion object TrackServiceTestConstants {
        const val firstTrackName = "Test Track 1"
        const val firstTrackLatitude = 12.0
        const val firstTrackLongitude = 14.0

        const val secondTrackName = "Test Track 2"
        const val secondTrackLatitude = 30.0
        const val secondTrackLongitude = 33.0
    }
}
