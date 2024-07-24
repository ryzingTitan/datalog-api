package com.ryzingtitan.datalogapi.domain.sessionmetadata.dtos

import java.time.Instant
import java.util.UUID

data class SessionMetadata(
    val sessionId: UUID,
    val startTime: Instant,
    val endTime: Instant,
)
