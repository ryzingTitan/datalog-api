package com.ryzingtitan.datalogapi.domain.datalogrecord.services

import com.ryzingtitan.datalogapi.data.datalogrecord.repositories.DatalogRecordRepository
import com.ryzingtitan.datalogapi.domain.datalogrecord.dtos.DatalogRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class DatalogRecordService(private val datalogRecordRepository: DatalogRecordRepository) {
    @Cacheable(cacheNames = ["datalogs"], key = "#sessionId")
    fun getAllBySessionId(sessionId: UUID): Flow<DatalogRecord> {
        return datalogRecordRepository
            .findAllBySessionIdOrderByEpochMillisecondsAsc(sessionId)
            .map { datalogRecordEntity ->
                DatalogRecord(
                    datalogRecordEntity.sessionId,
                    Instant.ofEpochMilli(datalogRecordEntity.epochMilliseconds),
                    datalogRecordEntity.longitude,
                    datalogRecordEntity.latitude,
                    datalogRecordEntity.altitude,
                    datalogRecordEntity.intakeAirTemperature,
                    datalogRecordEntity.boostPressure,
                    datalogRecordEntity.coolantTemperature,
                    datalogRecordEntity.engineRpm,
                    datalogRecordEntity.speed,
                    datalogRecordEntity.throttlePosition,
                    datalogRecordEntity.airFuelRatio,
                )
            }
    }
}
