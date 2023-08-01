package com.ryzingtitan.datalogapi.data.datalog.repositories

import com.ryzingtitan.datalogapi.data.datalog.entities.DatalogEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DatalogRepository : CoroutineCrudRepository<DatalogEntity, String> {
    fun findAllBySessionIdOrderByEpochMillisecondsAsc(sessionId: UUID): Flow<DatalogEntity>
    fun deleteBySessionIdAndEpochMilliseconds(sessionId: UUID, epochMilliseconds: Long): Flow<DatalogEntity>
}
