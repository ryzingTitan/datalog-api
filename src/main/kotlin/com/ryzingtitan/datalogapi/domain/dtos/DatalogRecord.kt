package com.ryzingtitan.datalogapi.domain.dtos

import lombok.Generated
import java.time.Instant
import java.util.UUID

@Generated
data class DatalogRecord(
    val sessionId: UUID,
    val timestamp: Instant,
    val intakeAirTemperature: Double?
)
