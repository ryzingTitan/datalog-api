package com.ryzingtitan.datalogapi.domain.sessionmetadata.services

import com.ryzingtitan.datalogapi.data.datalogrecord.entities.DatalogRecordEntity
import com.ryzingtitan.datalogapi.data.datalogrecord.repositories.DatalogRecordRepository
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
        fun `returns correct session metadata when only one record exists for the session`() = runTest {
            whenever(mockDatalogRecordRepository.findAll()).thenReturn(flowOf(firstDatalogRecordEntity))

            val sessionMetadataList = sessionMetadataService.getAllSessionMetadata()

            assertEquals(listOf(firstSessionMetadata), sessionMetadataList.toList())
        }

        @Test
        fun `returns correct session metadata when multiple records exist for the session`() = runTest {
            whenever(mockDatalogRecordRepository.findAll())
                .thenReturn(flowOf(secondDatalogRecordEntity, thirdDatalogRecordEntity))

            val sessionMetadataList = sessionMetadataService.getAllSessionMetadata()

            assertEquals(listOf(secondSessionMetadata), sessionMetadataList.toList())
        }

        @Test
        fun `returns correct session metadata when multiple sessions exist`() = runTest {
            whenever(mockDatalogRecordRepository.findAll())
                .thenReturn(flowOf(firstDatalogRecordEntity, secondDatalogRecordEntity, thirdDatalogRecordEntity))

            val sessionMetadataList = sessionMetadataService.getAllSessionMetadata()

            assertEquals(listOf(firstSessionMetadata, secondSessionMetadata), sessionMetadataList.toList())
        }
    }

    @BeforeEach
    fun setup() {
        sessionMetadataService = SessionMetadataService(mockDatalogRecordRepository)

        whenever(mockDatalogRecordRepository.findAllBySessionId(firstSessionId))
            .thenReturn(flowOf(firstDatalogRecordEntity))
        whenever(mockDatalogRecordRepository.findAllBySessionId(secondSessionId))
            .thenReturn(flowOf(secondDatalogRecordEntity, thirdDatalogRecordEntity))
    }

    private lateinit var sessionMetadataService: SessionMetadataService

    private val mockDatalogRecordRepository = mock<DatalogRecordRepository>()
    private val firstSessionId = UUID.randomUUID()
    private val secondSessionId = UUID.randomUUID()
    private val firstSessionTimestamp = Instant.now()
    private val secondSessionStartTimestamp = Instant.now()
    private val secondSessionEndTimestamp = Instant.now().plusSeconds(500)

    private val firstDatalogRecordEntity = DatalogRecordEntity(
        sessionId = firstSessionId,
        timestamp = firstSessionTimestamp,
        intakeAirTemperature = 135.9
    )

    private val secondDatalogRecordEntity = DatalogRecordEntity(
        sessionId = secondSessionId,
        timestamp = secondSessionStartTimestamp,
        intakeAirTemperature = 137.0
    )

    private val thirdDatalogRecordEntity = DatalogRecordEntity(
        sessionId = secondSessionId,
        timestamp = secondSessionEndTimestamp,
        intakeAirTemperature = 138.0
    )

    private val firstSessionMetadata = SessionMetadata(
        sessionId = firstSessionId,
        startTime = firstSessionTimestamp,
        endTime = firstSessionTimestamp
    )

    private val secondSessionMetadata = SessionMetadata(
        sessionId = secondSessionId,
        startTime = secondSessionStartTimestamp,
        endTime = secondSessionEndTimestamp
    )
}
