package com.ryzingtitan.datalogapi.domain.session.dtos

import kotlinx.coroutines.flow.Flow
import lombok.Generated
import org.springframework.core.io.buffer.DataBuffer

@Generated
data class FileUpload(
    val file: Flow<DataBuffer>,
    val metadata: FileUploadMetadata,
)
