package com.ryzingtitan.datalogapi.data.sessionmetadata.entities

import lombok.Generated
import java.util.*

@Generated
data class SessionMetadataEntity(
    val sessionId: UUID,
    val startTimeEpochMilliseconds: Long,
    val endTimeEpochMilliseconds: Long,
    val username: String,
)
