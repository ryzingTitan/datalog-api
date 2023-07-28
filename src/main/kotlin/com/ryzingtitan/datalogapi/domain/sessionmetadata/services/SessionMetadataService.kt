package com.ryzingtitan.datalogapi.domain.sessionmetadata.services

import com.ryzingtitan.datalogapi.data.sessionmetadata.repositories.SessionMetadataRepository
import com.ryzingtitan.datalogapi.domain.sessionmetadata.dtos.SessionMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class SessionMetadataService(
    private val sessionMetadataRepository: SessionMetadataRepository,
) {
    fun getAllSessionMetadataByUser(username: String): Flow<SessionMetadata> {
        return sessionMetadataRepository.getAllSessionMetadata()
            .filter { sessionMetadataEntity ->
                sessionMetadataEntity.username == username
            }
            .map { sessionMetadataEntity ->
                SessionMetadata(
                    sessionId = sessionMetadataEntity.sessionId,
                    startTime = Instant.ofEpochMilli(sessionMetadataEntity.startTimeEpochMilliseconds),
                    endTime = Instant.ofEpochMilli(sessionMetadataEntity.endTimeEpochMilliseconds),
                )
            }
    }
}
