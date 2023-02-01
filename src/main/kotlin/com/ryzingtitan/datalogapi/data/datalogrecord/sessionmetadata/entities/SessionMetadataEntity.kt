package com.ryzingtitan.datalogapi.data.datalogrecord.sessionmetadata.entities

import java.util.*

data class SessionMetadataEntity(
    val sessionId: UUID,
    val startTimeEpochMilliseconds: Long,
    val endTimeEpochMilliseconds: Long,
)
