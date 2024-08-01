package com.ryzingtitan.datalogapi.data.datalogs.repositories

import com.ryzingtitan.datalogapi.data.datalogs.entities.DatalogEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DatalogRepository : CoroutineCrudRepository<DatalogEntity, Int> {
    suspend fun findAllBySessionIdOrderByTimestampAsc(sessionId: Int): Flow<DatalogEntity>

    suspend fun findAllBySessionId(sessionId: Int): Flow<DatalogEntity>

    suspend fun deleteAllBySessionId(sessionId: Int): Flow<DatalogEntity>
}
