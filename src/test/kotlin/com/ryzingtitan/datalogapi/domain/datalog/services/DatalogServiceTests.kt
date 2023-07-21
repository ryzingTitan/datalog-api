package com.ryzingtitan.datalogapi.domain.datalog.services

import com.ryzingtitan.datalogapi.data.datalog.entities.DataEntity
import com.ryzingtitan.datalogapi.data.datalog.entities.DatalogEntity
import com.ryzingtitan.datalogapi.data.datalog.entities.TrackInfoEntity
import com.ryzingtitan.datalogapi.data.datalog.entities.UserEntity
import com.ryzingtitan.datalogapi.data.datalog.repositories.DatalogRepository
import com.ryzingtitan.datalogapi.domain.datalog.dtos.Data
import com.ryzingtitan.datalogapi.domain.datalog.dtos.Datalog
import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
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
import java.time.temporal.ChronoUnit
import java.util.*

@ExperimentalCoroutinesApi
class DatalogServiceTests {
    @Nested
    inner class GetAllBySessionId {
        @Test
        fun `returns all datalog records with the session id that is provided`() = runTest {
            whenever(mockDatalogRepository.findAllBySessionIdOrderByEpochMillisecondsAsc(sessionId))
                .thenReturn(flowOf(firstDatalogEntity, secondDatalogEntity))

            val datalogs = datalogService.getAllBySessionId(sessionId)

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
    private val sessionId = UUID.randomUUID()
    private val timestamp = Instant.now()

    private val firstDatalogEntity = DatalogEntity(
        sessionId = sessionId,
        epochMilliseconds = timestamp.toEpochMilli(),
        data = DataEntity(
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
        ),
        trackInfo = TrackInfoEntity("", 0.0, 0.0),
        user = UserEntity("", "", ""),
    )

    private val secondDatalogEntity = DatalogEntity(
        sessionId = sessionId,
        epochMilliseconds = timestamp.toEpochMilli(),
        data = DataEntity(
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
        ),
        trackInfo = TrackInfoEntity("", 0.0, 0.0),
        user = UserEntity("", "", ""),
    )

    private val firstExpectedDatalog = Datalog(
        sessionId = sessionId,
        timestamp = timestamp.truncatedTo(ChronoUnit.MILLIS),
        data = Data(
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
        ),
        trackInfo = TrackInfo("", 0.0, 0.0),
        user = User("", "", ""),
    )

    private val secondExpectedDatalog = Datalog(
        sessionId = sessionId,
        timestamp = timestamp.truncatedTo(ChronoUnit.MILLIS),
        data = Data(
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
        ),
        trackInfo = TrackInfo("", 0.0, 0.0),
        user = User("", "", ""),
    )
}
