package com.ryzingtitan.datalogapi.domain.sessions.services

import com.ryzingtitan.datalogapi.domain.sessions.configuration.ColumnConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ColumnConfigurationServiceTests {
    @Nested
    inner class Create {
        @Test
        fun `creates correct column configuration from header row`() {
            val actualColumnConfiguration = columnConfigurationService.create(HEADER_ROW)

            assertEquals(expectedColumnConfiguration, actualColumnConfiguration)
        }
    }

    @BeforeEach
    fun setup() {
        columnConfigurationService = ColumnConfigurationService()
    }

    private lateinit var columnConfigurationService: ColumnConfigurationService

    private val expectedColumnConfiguration =
        ColumnConfiguration(
            deviceTime = 0,
            longitude = 1,
            latitude = 2,
            altitude = 3,
            coolantTemperature = 5,
            engineRpm = 6,
            intakeAirTemperature = 7,
            speed = 9,
            throttlePosition = 10,
            boostPressure = 11,
            airFuelRatio = 4,
        )

    companion object ColumnConfigurationServiceTestConstants {
        const val HEADER_ROW =
            "Device Time,Longitude,Latitude,Altitude,Air Fuel Ratio(Measured)(:1)," +
                "Engine Coolant Temperature(°F),Engine RPM(rpm),Intake Air Temperature(°F),GPS Speed (Meters/second)," +
                "Speed (OBD)(mph),Throttle Position(Manifold)(%),Turbo Boost & Vacuum Gauge(psi)"
    }
}
