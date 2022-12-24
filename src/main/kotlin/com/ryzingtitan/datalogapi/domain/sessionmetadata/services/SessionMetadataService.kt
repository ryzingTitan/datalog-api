package com.ryzingtitan.datalogapi.domain.sessionmetadata.services

import com.ryzingtitan.datalogapi.data.datalogrecord.entities.DatalogRecordEntity
import com.ryzingtitan.datalogapi.data.datalogrecord.repositories.DatalogRecordRepository
import com.ryzingtitan.datalogapi.domain.sessionmetadata.dtos.SessionMetadata
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.util.*

@Service
class SessionMetadataService(private val datalogRecordRepository: DatalogRecordRepository) {
    fun getAllSessionMetadata(): Flow<SessionMetadata> {
        val datalogRecordEntities = mutableListOf<DatalogRecordEntity>()
        runBlocking {
            datalogRecordEntities.addAll(datalogRecordRepository.findAll().toList())
        }
        return datalogRecordEntities.distinctBy { it.sessionId }
            .map { datalogRecordEntity ->
                getSessionMetadata(datalogRecordEntity.sessionId)
            }
            .asFlow()
    }

    private fun getSessionMetadata(sessionId: UUID): SessionMetadata {
        val datalogRecordEntities = mutableListOf<DatalogRecordEntity>()
        runBlocking {
            datalogRecordEntities.addAll(datalogRecordRepository.findAllBySessionIdOrderByTimestampAsc(sessionId).toList())
        }

        return SessionMetadata(
            sessionId,
            datalogRecordEntities.minOf { it.timestamp },
            datalogRecordEntities.maxOf { it.timestamp }
        )
    }
}
