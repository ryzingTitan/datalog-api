package com.ryzingtitan.datalogapi.domain.session.services

import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
import com.ryzingtitan.datalogapi.domain.session.configuration.ColumnConfiguration
import com.ryzingtitan.datalogapi.domain.session.dtos.FileUploadMetadata
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

@ExperimentalCoroutinesApi
class RowParsingServiceTests {
    @Nested
    inner class Parse {
        @Test
        fun `parses the row correctly when it contains valid session data`() = runTest {
            val row = "$firstLineDeviceTime," +
                "$firstLineLongitude," +
                "$firstLineLatitude," +
                "$firstLineAltitude," +
                "${firstLineCoolantTemperature.toFloat()}," +
                "${firstLineEngineRpm.toFloat()}," +
                "${firstLineIntakeAirTemperature.toFloat()}," +
                "${firstLineSpeed.toFloat()}," +
                "$firstLineThrottlePosition," +
                "$firstLineBoostPressure," +
                firstLineAirFuelRatio

            val datalog = rowParsingService.parse(
                row,
                fileUploadMetadata,
                columnConfiguration,
            )

            assertEquals(sessionId, datalog.sessionId)
            assertEquals(firstLineEpochMilliseconds, datalog.epochMilliseconds)
            assertEquals(firstLineLongitude, datalog.data.longitude)
            assertEquals(firstLineLatitude, datalog.data.latitude)
            assertEquals(firstLineAltitude, datalog.data.altitude)
            assertEquals(firstLineIntakeAirTemperature, datalog.data.intakeAirTemperature)
            assertEquals(firstLineBoostPressure, datalog.data.boostPressure)
            assertEquals(firstLineCoolantTemperature, datalog.data.coolantTemperature)
            assertEquals(firstLineEngineRpm, datalog.data.engineRpm)
            assertEquals(firstLineSpeed, datalog.data.speed)
            assertEquals(firstLineThrottlePosition, datalog.data.throttlePosition)
            assertEquals(firstLineAirFuelRatio, datalog.data.airFuelRatio)
            assertEquals("Test Track", datalog.trackInfo.name)
            assertEquals(42.4086, datalog.trackInfo.latitude)
            assertEquals(-86.1374, datalog.trackInfo.longitude)
            assertEquals("test@test.com", datalog.user.email)
            assertEquals("test", datalog.user.firstName)
            assertEquals("tester", datalog.user.lastName)
        }

        @Test
        fun `parses the row correctly when session id is null`() = runTest {
            val row = "$firstLineDeviceTime," +
                "$firstLineLongitude," +
                "$firstLineLatitude," +
                "$firstLineAltitude," +
                "${firstLineCoolantTemperature.toFloat()}," +
                "${firstLineEngineRpm.toFloat()}," +
                "${firstLineIntakeAirTemperature.toFloat()}," +
                "${firstLineSpeed.toFloat()}," +
                "$firstLineThrottlePosition," +
                "$firstLineBoostPressure," +
                firstLineAirFuelRatio

            val datalog = rowParsingService.parse(
                row,
                fileUploadMetadata.copy(sessionId = null),
                columnConfiguration,
            )

            assertNull(datalog.sessionId)
            assertEquals(firstLineEpochMilliseconds, datalog.epochMilliseconds)
            assertEquals(firstLineLongitude, datalog.data.longitude)
            assertEquals(firstLineLatitude, datalog.data.latitude)
            assertEquals(firstLineAltitude, datalog.data.altitude)
            assertEquals(firstLineIntakeAirTemperature, datalog.data.intakeAirTemperature)
            assertEquals(firstLineBoostPressure, datalog.data.boostPressure)
            assertEquals(firstLineCoolantTemperature, datalog.data.coolantTemperature)
            assertEquals(firstLineEngineRpm, datalog.data.engineRpm)
            assertEquals(firstLineSpeed, datalog.data.speed)
            assertEquals(firstLineThrottlePosition, datalog.data.throttlePosition)
            assertEquals(firstLineAirFuelRatio, datalog.data.airFuelRatio)
            assertEquals("Test Track", datalog.trackInfo.name)
            assertEquals(42.4086, datalog.trackInfo.latitude)
            assertEquals(-86.1374, datalog.trackInfo.longitude)
            assertEquals("test@test.com", datalog.user.email)
            assertEquals("test", datalog.user.firstName)
            assertEquals("tester", datalog.user.lastName)
        }

        @Test
        fun `parses the row correctly when it contains invalid session data`() = runTest {
            val row = "$secondLineDeviceTime," +
                "$secondLineLongitude," +
                "$secondLineLatitude," +
                "$secondLineAltitude," +
                "$secondLineCoolantTemperature," +
                "$secondLineEngineRpm," +
                "$secondLineIntakeAirTemperature," +
                "$secondLineSpeed," +
                "$secondLineThrottlePosition," +
                "$secondLineBoostPressure," +
                secondLineAirFuelRatio

            val datalog = rowParsingService.parse(
                row,
                fileUploadMetadata,
                columnConfiguration,
            )

            assertEquals(sessionId, datalog.sessionId)
            assertEquals(secondLineEpochMilliseconds, datalog.epochMilliseconds)
            assertEquals(secondLineLongitude, datalog.data.longitude)
            assertEquals(secondLineLatitude, datalog.data.latitude)
            assertEquals(secondLineAltitude, datalog.data.altitude)
            assertNull(datalog.data.intakeAirTemperature)
            assertNull(datalog.data.boostPressure)
            assertNull(datalog.data.coolantTemperature)
            assertNull(datalog.data.engineRpm)
            assertNull(datalog.data.speed)
            assertNull(datalog.data.throttlePosition)
            assertNull(datalog.data.airFuelRatio)
            assertEquals(trackName, datalog.trackInfo.name)
            assertEquals(trackLatitude, datalog.trackInfo.latitude)
            assertEquals(trackLongitude, datalog.trackInfo.longitude)
            assertEquals(userEmail, datalog.user.email)
            assertEquals(userFirstName, datalog.user.firstName)
            assertEquals(userLastName, datalog.user.lastName)
        }
    }

    @BeforeEach
    fun setup() {
        rowParsingService = RowParsingService()
    }

    private lateinit var rowParsingService: RowParsingService

    private val sessionId: UUID = UUID.fromString("c61cc339-f93d-45a4-aa2b-923f0482b97f")
    private val firstLineEpochMilliseconds = Instant.parse("2022-09-18T18:15:47.963Z").toEpochMilli()
    private val secondLineEpochMilliseconds = Instant.parse("2022-09-18T18:18:47.968Z").toEpochMilli()

    private val fileUploadMetadata = FileUploadMetadata(
        fileName = "testFile.txt",
        sessionId = sessionId,
        trackInfo = TrackInfo(
            name = trackName,
            latitude = trackLatitude,
            longitude = trackLongitude,
        ),
        user = User(
            email = userEmail,
            firstName = userFirstName,
            lastName = userLastName,
        ),
    )

    private val columnConfiguration = ColumnConfiguration(
        deviceTime = 0,
        longitude = 1,
        latitude = 2,
        altitude = 3,
        coolantTemperature = 4,
        engineRpm = 5,
        intakeAirTemperature = 6,
        speed = 7,
        throttlePosition = 8,
        boostPressure = 9,
        airFuelRatio = 10,
    )

    companion object RowParsingServiceTestConstants {
        const val firstLineDeviceTime = "18-Sep-2022 14:15:47.963"
        const val firstLineLongitude = -86.14162999999999
        const val firstLineLatitude = 42.406800000000004
        const val firstLineAltitude = 188.4f
        const val firstLineIntakeAirTemperature = 123
        const val firstLineBoostPressure = 16.5f
        const val firstLineCoolantTemperature = 155
        const val firstLineEngineRpm = 5500
        const val firstLineSpeed = 86
        const val firstLineThrottlePosition = 95.5f
        const val firstLineAirFuelRatio = 14.7f

        const val secondLineDeviceTime = "18-Sep-2022 14:18:47.968"
        const val secondLineLongitude = 86.14162999999999
        const val secondLineLatitude = -42.406800000000004
        const val secondLineAltitude = 188.0f
        const val secondLineIntakeAirTemperature = "-"
        const val secondLineBoostPressure = "-"
        const val secondLineCoolantTemperature = "-"
        const val secondLineEngineRpm = "-"
        const val secondLineSpeed = "-"
        const val secondLineThrottlePosition = "-"
        const val secondLineAirFuelRatio = "-"

        const val trackName = "Test Track"
        const val trackLatitude = 42.4086
        const val trackLongitude = -86.1374

        const val userEmail = "test@test.com"
        const val userFirstName = "test"
        const val userLastName = "tester"
    }
}
