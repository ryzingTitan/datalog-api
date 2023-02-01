package com.ryzingtitan.datalogapi.domain.sessionmetadata.services

import com.ryzingtitan.datalogapi.data.datalogrecord.sessionmetadata.entities.SessionMetadataEntity
import com.ryzingtitan.datalogapi.data.datalogrecord.sessionmetadata.repositories.SessionMetadataRepository
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
import java.util.UUID

@ExperimentalCoroutinesApi
class SessionMetadataServiceTests {
    @Nested
    inner class GetAllSessionMetadata {
        @Test
        fun `returns correct session metadata`() = runTest {
            val sessionMetadataList = sessionMetadataService.getAllSessionMetadata()

            assertEquals(listOf(firstSessionMetadata, secondSessionMetadata), sessionMetadataList.toList())
        }
    }

    @BeforeEach
    fun setup() {
        sessionMetadataService = SessionMetadataService(mockSessionMetadataRepository)

        whenever(mockSessionMetadataRepository.getAllSessionMetadata())
            .thenReturn(flowOf(firstSessionMetadataEntity, secondSessionMetadataEntity))
    }

    private lateinit var sessionMetadataService: SessionMetadataService

    private val mockSessionMetadataRepository = mock<SessionMetadataRepository>()
    private val firstSessionId = UUID.randomUUID()
    private val secondSessionId = UUID.randomUUID()
    private val firstSessionTimestamp = Instant.now()
    private val secondSessionStartTimestamp = Instant.now()
    private val secondSessionEndTimestamp = Instant.now().plusSeconds(500)

    private val firstSessionMetadata = SessionMetadata(
        sessionId = firstSessionId,
        startTime = firstSessionTimestamp,
        endTime = firstSessionTimestamp,
    )

    private val firstSessionMetadataEntity = SessionMetadataEntity(
        sessionId = firstSessionId,
        startTime = firstSessionTimestamp,
        endTime = firstSessionTimestamp,
    )

    private val secondSessionMetadata = SessionMetadata(
        sessionId = secondSessionId,
        startTime = secondSessionStartTimestamp,
        endTime = secondSessionEndTimestamp,
    )

    private val secondSessionMetadataEntity = SessionMetadataEntity(
        sessionId = secondSessionId,
        startTime = secondSessionStartTimestamp,
        endTime = secondSessionEndTimestamp,
    )
}
