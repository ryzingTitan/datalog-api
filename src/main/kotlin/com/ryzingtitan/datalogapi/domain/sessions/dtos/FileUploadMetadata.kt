package com.ryzingtitan.datalogapi.domain.sessions.dtos

data class FileUploadMetadata(
    val fileName: String,
    val sessionId: Int?,
    val trackId: Int,
    val userEmail: String,
    val userFirstName: String,
    val userLastName: String,
)
