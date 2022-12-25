package com.ryzingtitan.datalogapi.domain.datalogrecord.services

import com.ryzingtitan.datalogapi.data.datalogrecord.entities.DatalogRecordEntity
import com.ryzingtitan.datalogapi.data.datalogrecord.repositories.DatalogRecordRepository
import com.ryzingtitan.datalogapi.domain.datalogrecord.dtos.DatalogRecord
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import java.time.Instant
import java.util.*

@ExperimentalCoroutinesApi
class DatalogRecordServiceTests {
    @Nested
    inner class GetAllBySessionId {
        @Test
        fun `returns all datalog records with the session id that is provided`() = runTest {
            whenever(mockDatalogRecordRepository.findAllBySessionIdOrderByTimestampAsc(sessionId))
                .thenReturn(flowOf(firstDatalogRecordEntity, secondDatalogRecordEntity))

            val datalogRecords = datalogRecordService.getAllBySessionId(sessionId)

            assertEquals(listOf(firstExpectedDatalogRecord, secondExpectedDatalogRecord), datalogRecords.toList())
        }
    }

    @BeforeEach
    fun setup() {
        datalogRecordService = DatalogRecordService(mockDatalogRecordRepository)
        reset(mockDatalogRecordRepository)
    }

    private lateinit var datalogRecordService: DatalogRecordService

    private val mockDatalogRecordRepository = mock<DatalogRecordRepository>()
    private val sessionId = UUID.randomUUID()
    private val timestamp = Instant.now()

    private val firstDatalogRecordEntity = DatalogRecordEntity(
        sessionId = sessionId,
        timestamp = timestamp,
        intakeAirTemperature = 135,
        boostPressure = 15.6f,
        coolantTemperature = 150,
        engineRpm = 5000
    )

    private val secondDatalogRecordEntity = DatalogRecordEntity(
        sessionId = sessionId,
        timestamp = timestamp,
        intakeAirTemperature = null,
        boostPressure = null,
        coolantTemperature = null,
        engineRpm = null
    )

    private val firstExpectedDatalogRecord = DatalogRecord(
        sessionId = sessionId,
        timestamp = timestamp,
        intakeAirTemperature = 135,
        boostPressure = 15.6f,
        coolantTemperature = 150,
        engineRpm = 5000
    )

    private val secondExpectedDatalogRecord = DatalogRecord(
        sessionId = sessionId,
        timestamp = timestamp,
        intakeAirTemperature = null,
        boostPressure = null,
        coolantTemperature = null,
        engineRpm = null
    )
}
