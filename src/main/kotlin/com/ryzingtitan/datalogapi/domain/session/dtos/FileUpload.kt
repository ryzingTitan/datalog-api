package com.ryzingtitan.datalogapi.domain.session.dtos

import kotlinx.coroutines.flow.Flow
import org.springframework.core.io.buffer.DataBuffer

data class FileUpload(
    val file: Flow<DataBuffer>,
    val metadata: FileUploadMetadata,
)
