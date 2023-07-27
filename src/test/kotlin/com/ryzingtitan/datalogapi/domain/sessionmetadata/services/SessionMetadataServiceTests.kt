package com.ryzingtitan.datalogapi.domain.sessionmetadata.services

import com.ryzingtitan.datalogapi.data.sessionmetadata.entities.SessionMetadataEntity
import com.ryzingtitan.datalogapi.data.sessionmetadata.repositories.SessionMetadataRepository
import com.ryzingtitan.datalogapi.domain.sessionmetadata.dtos.SessionMetadata
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@ExperimentalCoroutinesApi
class SessionMetadataServiceTests {
    @Nested
    inner class GetAllSessionMetadataByUser {
        @Test
        fun `returns correct session metadata`() = runTest {
            val sessionMetadataList = sessionMetadataService.getAllSessionMetadataByUser(firstUsername)

            assertEquals(listOf(firstSessionMetadata, secondSessionMetadata), sessionMetadataList.toList())
        }
    }

    @BeforeEach
    fun setup() {
        sessionMetadataService = SessionMetadataService(mockSessionMetadataRepository)

        whenever(mockSessionMetadataRepository.getAllSessionMetadata())
            .thenReturn(flowOf(firstSessionMetadataEntity, secondSessionMetadataEntity, thirdSessionMetadataEntity))
    }

    private lateinit var sessionMetadataService: SessionMetadataService

    private val mockSessionMetadataRepository = mock<SessionMetadataRepository>()
    private val firstSessionId = UUID.randomUUID()
    private val secondSessionId = UUID.randomUUID()
    private val thirdSessionId = UUID.randomUUID()
    private val firstSessionTimestamp = Instant.now()
    private val secondSessionStartTimestamp = Instant.now()
    private val secondSessionEndTimestamp = Instant.now().plusSeconds(500)
    private val thirdSessionTimestamp = Instant.now()
    private val firstUsername = "test@test.com"
    private val secondUsername = "test2@test.com"

    private val firstSessionMetadata = SessionMetadata(
        sessionId = firstSessionId,
        startTime = firstSessionTimestamp.truncatedTo(ChronoUnit.MILLIS),
        endTime = firstSessionTimestamp.truncatedTo(ChronoUnit.MILLIS),
    )

    private val firstSessionMetadataEntity = SessionMetadataEntity(
        sessionId = firstSessionId,
        startTimeEpochMilliseconds = firstSessionTimestamp.toEpochMilli(),
        endTimeEpochMilliseconds = firstSessionTimestamp.toEpochMilli(),
        username = firstUsername,
    )

    private val secondSessionMetadata = SessionMetadata(
        sessionId = secondSessionId,
        startTime = secondSessionStartTimestamp.truncatedTo(ChronoUnit.MILLIS),
        endTime = secondSessionEndTimestamp.truncatedTo(ChronoUnit.MILLIS),
    )

    private val secondSessionMetadataEntity = SessionMetadataEntity(
        sessionId = secondSessionId,
        startTimeEpochMilliseconds = secondSessionStartTimestamp.toEpochMilli(),
        endTimeEpochMilliseconds = secondSessionEndTimestamp.toEpochMilli(),
        username = firstUsername,
    )

    private val thirdSessionMetadataEntity = SessionMetadataEntity(
        sessionId = thirdSessionId,
        startTimeEpochMilliseconds = thirdSessionTimestamp.toEpochMilli(),
        endTimeEpochMilliseconds = thirdSessionTimestamp.toEpochMilli(),
        username = secondUsername,
    )
}
