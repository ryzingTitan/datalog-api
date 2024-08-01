package com.ryzingtitan.datalogapi.domain.sessions.services

import com.ryzingtitan.datalogapi.data.cars.repositories.CarRepository
import com.ryzingtitan.datalogapi.data.datalogs.repositories.DatalogRepository
import com.ryzingtitan.datalogapi.data.sessions.entities.SessionEntity
import com.ryzingtitan.datalogapi.data.sessions.repositories.SessionRepository
import com.ryzingtitan.datalogapi.data.tracks.repositories.TrackRepository
import com.ryzingtitan.datalogapi.domain.sessions.dtos.FileUpload
import com.ryzingtitan.datalogapi.domain.sessions.dtos.Session
import com.ryzingtitan.datalogapi.domain.sessions.exceptions.SessionAlreadyExistsException
import com.ryzingtitan.datalogapi.domain.sessions.exceptions.SessionDoesNotExistException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SessionService(
    private val sessionRepository: SessionRepository,
    private val trackRepository: TrackRepository,
    private val carRepository: CarRepository,
    private val fileParsingService: FileParsingService,
    private val datalogRepository: DatalogRepository,
) {
    suspend fun getAllByUser(userEmail: String): Flow<Session> {
        return sessionRepository.findAllByUserEmail(userEmail).map { sessionEntity ->
            val trackEntity = trackRepository.findById(sessionEntity.trackId)!!
            val carEntity = carRepository.findById(sessionEntity.carId)!!

            Session(
                id = sessionEntity.id!!,
                startTime = sessionEntity.startTime,
                endTime = sessionEntity.endTime,
                trackName = trackEntity.name,
                trackLatitude = trackEntity.latitude,
                trackLongitude = trackEntity.longitude,
                carYear = carEntity.yearManufactured,
                carMake = carEntity.make,
                carModel = carEntity.model,
            )
        }
    }

    suspend fun create(fileUpload: FileUpload): Int {
        val datalogs = fileParsingService.parse(fileUpload)

        val firstDatalogTimestamp = datalogs.minBy { it.timestamp }.timestamp
        val lastDatalogTimestamp = datalogs.maxBy { it.timestamp }.timestamp
        val existingSession =
            sessionRepository.findByUserEmailAndStartTimeAndEndTime(
                fileUpload.metadata.userEmail,
                firstDatalogTimestamp,
                lastDatalogTimestamp,
            )

        if (existingSession != null) {
            val message = "A session already exists for this user and timestamp"
            logger.error(message)
            throw SessionAlreadyExistsException(message)
        }

        val sessionId =
            sessionRepository.save(
                SessionEntity(
                    userEmail = fileUpload.metadata.userEmail,
                    userFirstName = fileUpload.metadata.userFirstName,
                    userLastName = fileUpload.metadata.userLastName,
                    startTime = firstDatalogTimestamp,
                    endTime = lastDatalogTimestamp,
                    trackId = fileUpload.metadata.trackId,
                    carId = fileUpload.metadata.carId,
                ),
            ).id!!

        datalogRepository.saveAll(datalogs.map { it.copy(sessionId = sessionId) }).collect()

        logger.info("Session $sessionId created")
        return sessionId
    }

    suspend fun update(fileUpload: FileUpload) {
        val existingDatalogs = datalogRepository.findAllBySessionId(fileUpload.metadata.sessionId!!)

        if (existingDatalogs.count() == 0) {
            val message = "Session id ${fileUpload.metadata.sessionId} does not exist"
            logger.error(message)
            throw SessionDoesNotExistException(message)
        }

        datalogRepository.deleteAllBySessionId(fileUpload.metadata.sessionId).collect()

        val newDatalogs = fileParsingService.parse(fileUpload)

        datalogRepository.saveAll(newDatalogs).collect()
        logger.info("Session ${fileUpload.metadata.sessionId} updated")
    }

    private val logger: Logger = LoggerFactory.getLogger(SessionService::class.java)
}
