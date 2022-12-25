package com.ryzingtitan.datalogapi.domain.sessionmetadata.dtos

import lombok.Generated
import java.time.Instant
import java.util.UUID

@Generated
data class SessionMetadata(
    val sessionId: UUID,
    val startTime: Instant,
    val endTime: Instant
)
