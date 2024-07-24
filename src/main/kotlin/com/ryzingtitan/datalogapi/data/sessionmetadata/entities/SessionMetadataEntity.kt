package com.ryzingtitan.datalogapi.data.sessionmetadata.entities

import java.util.UUID

data class SessionMetadataEntity(
    val sessionId: UUID,
    val startTimeEpochMilliseconds: Long,
    val endTimeEpochMilliseconds: Long,
    val username: String,
)
