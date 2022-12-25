package com.ryzingtitan.datalogapi.data.datalogrecord.sessionmetadata.entities

import java.time.Instant
import java.util.*

data class SessionMetadataEntity(
    val sessionId: UUID,
    val startTime: Instant,
    val endTime: Instant
)
