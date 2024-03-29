package com.ryzingtitan.datalogapi.domain.session.dtos

import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
import lombok.Generated
import java.util.UUID

@Generated
data class FileUploadMetadata(
    val fileName: String,
    val sessionId: UUID?,
    val trackInfo: TrackInfo,
    val user: User,
)
