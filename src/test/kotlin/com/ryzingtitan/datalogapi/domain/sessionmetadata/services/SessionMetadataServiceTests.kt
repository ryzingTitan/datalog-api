package com.ryzingtitan.datalogapi.domain.sessionmetadata.services

import com.ryzingtitan.datalogapi.data.sessionmetadata.entities.SessionMetadataEntity
import com.ryzingtitan.datalogapi.data.sessionmetadata.repositories.SessionMetadataRepository
import com.ryzingtitan.datalogapi.domain.sessionmetadata.dtos.SessionMetadata
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
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
        fun `returns correct session metadata`() =
            runTest {
                val sessionMetadataList = sessionMetadataService.getAllSessionMetadataByUser(firstUsername)

                assertEquals(listOf(firstSessionMetadata, secondSessionMetadata), sessionMetadataList.toList())
            }
    }

    @Nested
    inner class GetExistingSessionId {
        @Test
        fun `returns existing session id when session already exists`() =
            runTest {
                val sessionId =
                    sessionMetadataService.getExistingSessionId(
                        firstUsername,
                        startTime.plusSeconds(25).toEpochMilli(),
                    )

                assertEquals(secondSessionMetadata.sessionId, sessionId)
            }

        @Test
        fun `returns null when session already exists`() =
            runTest {
                val sessionId =
                    sessionMetadataService.getExistingSessionId(
                        firstUsername,
                        startTime.plusSeconds(80).toEpochMilli(),
                    )

                assertNull(sessionId)
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
    private val startTime = Instant.now()
    private val firstSessionStartTimestamp = startTime
    private val firstSessionEndTimestamp = startTime.plusSeconds(10)
    private val secondSessionStartTimestamp = startTime.plusSeconds(20)
    private val secondSessionEndTimestamp = startTime.plusSeconds(60)
    private val thirdSessionStartTimestamp = startTime.plusSeconds(20)
    private val thirdSessionEndTimestamp = startTime.plusSeconds(60)
    private val firstUsername = "test@test.com"
    private val secondUsername = "test2@test.com"

    private val firstSessionMetadata =
        SessionMetadata(
            sessionId = firstSessionId,
            startTime = firstSessionStartTimestamp.truncatedTo(ChronoUnit.MILLIS),
            endTime = firstSessionEndTimestamp.truncatedTo(ChronoUnit.MILLIS),
        )

    private val firstSessionMetadataEntity =
        SessionMetadataEntity(
            sessionId = firstSessionId,
            startTimeEpochMilliseconds = firstSessionStartTimestamp.toEpochMilli(),
            endTimeEpochMilliseconds = firstSessionEndTimestamp.toEpochMilli(),
            username = firstUsername,
        )

    private val secondSessionMetadata =
        SessionMetadata(
            sessionId = secondSessionId,
            startTime = secondSessionStartTimestamp.truncatedTo(ChronoUnit.MILLIS),
            endTime = secondSessionEndTimestamp.truncatedTo(ChronoUnit.MILLIS),
        )

    private val secondSessionMetadataEntity =
        SessionMetadataEntity(
            sessionId = secondSessionId,
            startTimeEpochMilliseconds = secondSessionStartTimestamp.toEpochMilli(),
            endTimeEpochMilliseconds = secondSessionEndTimestamp.toEpochMilli(),
            username = firstUsername,
        )

    private val thirdSessionMetadataEntity =
        SessionMetadataEntity(
            sessionId = thirdSessionId,
            startTimeEpochMilliseconds = thirdSessionStartTimestamp.toEpochMilli(),
            endTimeEpochMilliseconds = thirdSessionEndTimestamp.toEpochMilli(),
            username = secondUsername,
        )
}
