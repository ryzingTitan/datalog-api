package com.ryzingtitan.datalogapi.domain.datalog.dtos

import lombok.Generated
import java.time.Instant
import java.util.UUID

@Generated
data class Datalog(
    val sessionId: UUID,
    val timestamp: Instant,
    val data: Data,
    val trackInfo: TrackInfo,
    val user: User,
)
