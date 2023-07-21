package com.ryzingtitan.datalogapi.data.datalog.entities

import lombok.Generated

@Generated
data class DataEntity(
    val longitude: Double,
    val latitude: Double,
    val altitude: Float,
    val intakeAirTemperature: Int?,
    val boostPressure: Float?,
    val coolantTemperature: Int?,
    val engineRpm: Int?,
    val speed: Int?,
    val throttlePosition: Float?,
    val airFuelRatio: Float?,
)
