package com.ryzingtitan.datalogapi.domain.datalog.dtos

import lombok.Generated
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.util.UUID

@Generated
@Document
data class Datalog(
    val sessionId: UUID,
    val timestamp: Instant,
    val data: Data,
    val trackInfo: TrackInfo,
    val user: User,
)
