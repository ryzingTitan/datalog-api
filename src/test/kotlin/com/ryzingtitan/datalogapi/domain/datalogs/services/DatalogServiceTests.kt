package com.ryzingtitan.datalogapi.domain.datalogs.services

import com.ryzingtitan.datalogapi.data.datalogs.entities.DatalogEntity
import com.ryzingtitan.datalogapi.data.datalogs.repositories.DatalogRepository
import com.ryzingtitan.datalogapi.domain.datalogs.dtos.Datalog
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
import java.time.temporal.ChronoUnit

class DatalogServiceTests {
    @Nested
    inner class GetAllBySessionId {
        @Test
        fun `returns all datalogs with the session id that is provided`() =
            runTest {
                whenever(mockDatalogRepository.findAllBySessionIdOrderByTimestampAsc(SESSION_ID))
                    .thenReturn(flowOf(firstDatalogEntity, secondDatalogEntity))

                val datalogs = datalogService.getAllBySessionId(SESSION_ID)

                assertEquals(listOf(firstExpectedDatalog, secondExpectedDatalog), datalogs.toList())
            }
    }

    @BeforeEach
    fun setup() {
        datalogService = DatalogService(mockDatalogRepository)
        reset(mockDatalogRepository)
    }

    private lateinit var datalogService: DatalogService

    private val mockDatalogRepository = mock<DatalogRepository>()
    private val timestamp = Instant.now()

    private val firstDatalogEntity =
        DatalogEntity(
            sessionId = SESSION_ID,
            timestamp = timestamp,
            longitude = -86.14162,
            latitude = 42.406800000000004,
            altitude = 188.4f,
            intakeAirTemperature = 135,
            boostPressure = 15.6f,
            coolantTemperature = 150,
            engineRpm = 5000,
            speed = 85,
            throttlePosition = 75.6f,
            airFuelRatio = 15.8f,
        )

    private val secondDatalogEntity =
        DatalogEntity(
            sessionId = SESSION_ID,
            timestamp = timestamp,
            longitude = 86.14162,
            latitude = -42.406800000000004,
            altitude = 188.0f,
            intakeAirTemperature = null,
            boostPressure = null,
            coolantTemperature = null,
            engineRpm = null,
            speed = null,
            throttlePosition = null,
            airFuelRatio = null,
        )

    private val firstExpectedDatalog =
        Datalog(
            sessionId = SESSION_ID,
            timestamp = timestamp.truncatedTo(ChronoUnit.MILLIS),
            longitude = -86.14162,
            latitude = 42.406800000000004,
            altitude = 188.4f,
            intakeAirTemperature = 135,
            boostPressure = 15.6f,
            coolantTemperature = 150,
            engineRpm = 5000,
            speed = 85,
            throttlePosition = 75.6f,
            airFuelRatio = 15.8f,
        )

    private val secondExpectedDatalog =
        Datalog(
            sessionId = SESSION_ID,
            timestamp = timestamp.truncatedTo(ChronoUnit.MILLIS),
            longitude = 86.14162,
            latitude = -42.406800000000004,
            altitude = 188.0f,
            intakeAirTemperature = null,
            boostPressure = null,
            coolantTemperature = null,
            engineRpm = null,
            speed = null,
            throttlePosition = null,
            airFuelRatio = null,
        )

    companion object DatalogServiceTestConstants {
        const val SESSION_ID = 1
    }
}
