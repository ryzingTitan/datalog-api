package com.ryzingtitan.datalogapi.domain.session.services

import com.ryzingtitan.datalogapi.data.datalog.entities.DataEntity
import com.ryzingtitan.datalogapi.data.datalog.entities.DatalogEntity
import com.ryzingtitan.datalogapi.data.datalog.entities.TrackInfoEntity
import com.ryzingtitan.datalogapi.data.datalog.entities.UserEntity
import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
import com.ryzingtitan.datalogapi.domain.session.configuration.ColumnConfiguration
import com.ryzingtitan.datalogapi.domain.session.dtos.FileUploadMetadata
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Service
class RowParsingService {
    private val logger = LoggerFactory.getLogger(RowParsingService::class.java)

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    suspend fun parse(
        row: String,
        metadata: FileUploadMetadata,
        columnConfiguration: ColumnConfiguration,
    ): DatalogEntity? {
        return try {
            createDatalog(row, metadata, columnConfiguration)
        } catch (exception: Exception) {
            logger.error("Unable to parse row: $row")
            null
        }
    }

    private suspend fun createDatalog(
        row: String,
        metadata: FileUploadMetadata,
        columnConfiguration: ColumnConfiguration,
    ): DatalogEntity {
        val lineColumns = row.split(',')

        val datalogTimestamp = parseRowTimestamp(lineColumns[columnConfiguration.deviceTime])

        return DatalogEntity(
            sessionId = metadata.sessionId,
            epochMilliseconds = datalogTimestamp.toEpochMilli(),
            data = getData(lineColumns, columnConfiguration),
            trackInfo = getTrackInfo(metadata.trackInfo),
            user = getUser(metadata.user),
        )
    }

    private fun parseRowTimestamp(rowTimestamp: String): Instant {
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").withZone(ZoneId.of("America/New_York"))
        val parsedDateTime = dateTimeFormatter.parse(rowTimestamp)
        return Instant.from(parsedDateTime)
    }

    private fun getData(
        lineColumns: List<String>,
        columnConfiguration: ColumnConfiguration,
    ): DataEntity {
        val longitude = lineColumns[columnConfiguration.longitude].toDouble()
        val latitude = lineColumns[columnConfiguration.latitude].toDouble()
        val altitude = lineColumns[columnConfiguration.altitude].toFloat()
        val intakeAirTemperature = lineColumns[columnConfiguration.intakeAirTemperature].toFloatOrNull()?.toInt()
        val boostPressure = lineColumns[columnConfiguration.boostPressure].toFloatOrNull()
        val coolantTemperature = lineColumns[columnConfiguration.coolantTemperature].toFloatOrNull()?.toInt()
        val engineRpm = lineColumns[columnConfiguration.engineRpm].toFloatOrNull()?.toInt()
        val speed = lineColumns[columnConfiguration.speed].toFloatOrNull()?.toInt()
        val throttlePosition = lineColumns[columnConfiguration.throttlePosition].toFloatOrNull()
        val airFuelRatio = lineColumns[columnConfiguration.airFuelRatio].toFloatOrNull()

        return DataEntity(
            longitude = longitude,
            latitude = latitude,
            altitude = altitude,
            intakeAirTemperature = intakeAirTemperature,
            boostPressure = boostPressure,
            coolantTemperature = coolantTemperature,
            engineRpm = engineRpm,
            speed = speed,
            throttlePosition = throttlePosition,
            airFuelRatio = airFuelRatio,
        )
    }

    private fun getTrackInfo(trackInfo: TrackInfo): TrackInfoEntity {
        return TrackInfoEntity(
            name = trackInfo.name,
            latitude = trackInfo.latitude,
            longitude = trackInfo.longitude,
        )
    }

    private fun getUser(user: User): UserEntity {
        return UserEntity(
            email = user.email,
            firstName = user.firstName,
            lastName = user.lastName,
        )
    }
}
