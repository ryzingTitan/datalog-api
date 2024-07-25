package com.ryzingtitan.datalogapi.domain.sessions.services

import com.ryzingtitan.datalogapi.domain.sessions.configuration.ColumnConfiguration
import org.springframework.stereotype.Service

@Service
class ColumnConfigurationService {
    fun create(headerRow: String): ColumnConfiguration {
        val columns = mutableListOf<String>()

        headerRow.split(",").forEach { columnHeader ->
            columns.add(columnHeader.trim())
        }

        return ColumnConfiguration(
            deviceTime = columns.indexOf("Device Time"),
            longitude = columns.indexOf("Longitude"),
            latitude = columns.indexOf("Latitude"),
            altitude = columns.indexOf("Altitude"),
            intakeAirTemperature = columns.indexOf("Intake Air Temperature(°F)"),
            boostPressure = columns.indexOf("Turbo Boost & Vacuum Gauge(psi)"),
            coolantTemperature = columns.indexOf("Engine Coolant Temperature(°F)"),
            engineRpm = columns.indexOf("Engine RPM(rpm)"),
            speed = columns.indexOf("Speed (OBD)(mph)"),
            throttlePosition = columns.indexOf("Throttle Position(Manifold)(%)"),
            airFuelRatio = columns.indexOf("Air Fuel Ratio(Measured)(:1)"),
        )
    }
}
