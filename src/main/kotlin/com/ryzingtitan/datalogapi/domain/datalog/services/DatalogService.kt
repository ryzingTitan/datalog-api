package com.ryzingtitan.datalogapi.domain.datalog.services

import com.ryzingtitan.datalogapi.data.datalog.repositories.DatalogRepository
import com.ryzingtitan.datalogapi.domain.datalog.dtos.Data
import com.ryzingtitan.datalogapi.domain.datalog.dtos.Datalog
import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class DatalogService(private val datalogRepository: DatalogRepository) {
    @Cacheable(cacheNames = ["datalogs"], key = "#sessionId")
    fun getAllBySessionId(sessionId: UUID): Flow<Datalog> {
        return datalogRepository
            .findAllBySessionIdOrderByEpochMillisecondsAsc(sessionId)
            .map { datalogEntity ->
                Datalog(
                    datalogEntity.sessionId!!,
                    Instant.ofEpochMilli(datalogEntity.epochMilliseconds),
                    Data(
                        datalogEntity.data.longitude,
                        datalogEntity.data.latitude,
                        datalogEntity.data.altitude,
                        datalogEntity.data.intakeAirTemperature,
                        datalogEntity.data.boostPressure,
                        datalogEntity.data.coolantTemperature,
                        datalogEntity.data.engineRpm,
                        datalogEntity.data.speed,
                        datalogEntity.data.throttlePosition,
                        datalogEntity.data.airFuelRatio,
                    ),
                    TrackInfo(
                        datalogEntity.trackInfo.name,
                        datalogEntity.trackInfo.latitude,
                        datalogEntity.trackInfo.longitude,
                    ),
                    User(
                        datalogEntity.user.firstName,
                        datalogEntity.user.lastName,
                        datalogEntity.user.email,
                    ),
                )
            }
    }
}
