package com.ryzingtitan.datalogapi.data.datalogrecord.entities

import lombok.Generated
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Generated
@Document("datalogRecord")
data class DatalogRecordEntity(
    @Id val recordId: UUID = UUID.randomUUID(),
    val sessionId: UUID,
    val timestamp: Instant,
    val intakeAirTemperature: Double?
)
