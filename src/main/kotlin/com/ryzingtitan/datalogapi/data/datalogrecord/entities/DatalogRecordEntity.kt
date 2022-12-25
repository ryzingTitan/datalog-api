package com.ryzingtitan.datalogapi.data.datalogrecord.entities

import lombok.Generated
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.IndexDirection
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.*

@Generated
@Document("datalogRecord")
data class DatalogRecordEntity(
    @Id val recordId: UUID = UUID.randomUUID(),
    @Indexed val sessionId: UUID,
    @Indexed(direction = IndexDirection.ASCENDING) val timestamp: Instant,
    val intakeAirTemperature: Int?,
    val boostPressure: Float?,
    val coolantTemperature: Int?,
    val engineRpm: Int?
)
