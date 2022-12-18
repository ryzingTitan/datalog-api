package com.ryzingtitan.datalogapi.domain.services

import com.ryzingtitan.datalogapi.data.datalogrecord.repositories.DatalogRecordRepository
import com.ryzingtitan.datalogapi.domain.dtos.DatalogRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DatalogRecordService(private val datalogRecordRepository: DatalogRecordRepository) {
    fun getAllBySessionId(sessionId: UUID): Flow<DatalogRecord> {
        return datalogRecordRepository
            .findAllBySessionId(sessionId)
            .map { datalogRecordEntity ->
                DatalogRecord(
                    datalogRecordEntity.sessionId,
                    datalogRecordEntity.timestamp,
                    datalogRecordEntity.intakeAirTemperature
                )
            }
    }
}
