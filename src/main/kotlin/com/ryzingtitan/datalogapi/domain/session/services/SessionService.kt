package com.ryzingtitan.datalogapi.domain.session.services

import com.ryzingtitan.datalogapi.data.datalog.repositories.DatalogRepository
import com.ryzingtitan.datalogapi.domain.session.dtos.FileUpload
import com.ryzingtitan.datalogapi.domain.session.exceptions.SessionAlreadyExistsException
import com.ryzingtitan.datalogapi.domain.session.exceptions.SessionDoesNotExistException
import com.ryzingtitan.datalogapi.domain.sessionmetadata.services.SessionMetadataService
import com.ryzingtitan.datalogapi.domain.uuid.UuidGenerator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SessionService(
    private val fileParsingService: FileParsingService,
    private val datalogRepository: DatalogRepository,
    private val uuidGenerator: UuidGenerator,
    private val sessionMetadataService: SessionMetadataService,
) {
    private val logger: Logger = LoggerFactory.getLogger(SessionService::class.java)

    suspend fun create(fileUpload: FileUpload): UUID {
        val datalogs = fileParsingService.parse(fileUpload)

        val firstDatalogTimestamp = datalogs.minBy { it.epochMilliseconds }.epochMilliseconds
        val existingSessionId =
            sessionMetadataService.getExistingSessionId(fileUpload.metadata.user.email, firstDatalogTimestamp)

        if (existingSessionId != null) {
            val message = "A session already exists for this user and timestamp"
            logger.error(message)
            throw SessionAlreadyExistsException(message)
        }

        val sessionId = uuidGenerator.generate()
        datalogRepository.saveAll(datalogs.map { it.copy(sessionId = sessionId) }).collect()

        logger.info("Session $sessionId created")
        return sessionId
    }

    suspend fun update(fileUpload: FileUpload) {
        val existingDatalogs = datalogRepository.findAllBySessionId(fileUpload.metadata.sessionId!!)
        val datalogCount = existingDatalogs.count()

        if (datalogCount == 0) {
            val message = "Session id ${fileUpload.metadata.sessionId} does not exist"
            logger.error(message)
            throw SessionDoesNotExistException(message)
        }

        datalogRepository.deleteBySessionId(fileUpload.metadata.sessionId).collect()

        val newDatalogs = fileParsingService.parse(fileUpload)

        datalogRepository.saveAll(newDatalogs).collect()
        logger.info("Session ${fileUpload.metadata.sessionId} updated")
    }
}
