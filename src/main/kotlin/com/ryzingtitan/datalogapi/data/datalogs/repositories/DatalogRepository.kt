package com.ryzingtitan.datalogapi.data.datalogs.repositories

import com.ryzingtitan.datalogapi.data.datalogs.entities.DatalogEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface DatalogRepository : CoroutineCrudRepository<DatalogEntity, Int> {
    fun findAllBySessionIdOrderByTimestampAsc(sessionId: Int): Flow<DatalogEntity>

    fun findAllBySessionId(sessionId: Int): Flow<DatalogEntity>

    fun deleteBySessionId(sessionId: Int): Flow<DatalogEntity>
}
