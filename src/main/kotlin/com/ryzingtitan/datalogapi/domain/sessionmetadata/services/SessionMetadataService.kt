package com.ryzingtitan.datalogapi.domain.sessionmetadata.services

import com.ryzingtitan.datalogapi.data.datalogrecord.sessionmetadata.repositories.SessionMetadataRepository
import com.ryzingtitan.datalogapi.domain.sessionmetadata.dtos.SessionMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class SessionMetadataService(
    private val sessionMetadataRepository: SessionMetadataRepository
) {
    fun getAllSessionMetadata(): Flow<SessionMetadata> {
        return sessionMetadataRepository.getAllSessionMetadata()
            .map { sessionMetadataEntity ->
                SessionMetadata(
                    sessionId = sessionMetadataEntity.sessionId,
                    startTime = sessionMetadataEntity.startTime,
                    endTime = sessionMetadataEntity.endTime
                )
            }
    }
}
