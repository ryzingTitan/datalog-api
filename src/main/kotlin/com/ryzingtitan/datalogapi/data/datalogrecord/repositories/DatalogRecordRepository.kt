package com.ryzingtitan.datalogapi.data.datalogrecord.repositories

import com.ryzingtitan.datalogapi.data.datalogrecord.entities.DatalogRecordEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DatalogRecordRepository : CoroutineCrudRepository<DatalogRecordEntity, Int> {
    fun findAllBySessionId(sessionId: UUID): Flow<DatalogRecordEntity>
}
