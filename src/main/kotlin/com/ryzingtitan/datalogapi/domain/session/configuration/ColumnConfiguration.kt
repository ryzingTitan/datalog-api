package com.ryzingtitan.datalogapi.domain.session.configuration

import lombok.Generated

@Generated
@Suppress("LongParameterList")
data class ColumnConfiguration(
    val deviceTime: Int,
    val longitude: Int,
    val latitude: Int,
    val altitude: Int,
    val intakeAirTemperature: Int,
    val boostPressure: Int,
    val coolantTemperature: Int,
    val engineRpm: Int,
    val speed: Int,
    val throttlePosition: Int,
    val airFuelRatio: Int,
)
