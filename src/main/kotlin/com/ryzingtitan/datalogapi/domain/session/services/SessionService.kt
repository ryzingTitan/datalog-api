package com.ryzingtitan.datalogapi.domain.session.services

import com.ryzingtitan.datalogapi.data.datalog.repositories.DatalogRepository
import com.ryzingtitan.datalogapi.domain.exceptions.SessionAlreadyExistsException
import com.ryzingtitan.datalogapi.domain.exceptions.SessionDoesNotExistException
import com.ryzingtitan.datalogapi.domain.session.dtos.FileUpload
import com.ryzingtitan.datalogapi.domain.sessionmetadata.services.SessionMetadataService
import com.ryzingtitan.datalogapi.domain.uuid.UuidGenerator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SessionService(
    private val fileParsingService: FileParsingService,
    private val datalogRepository: DatalogRepository,
    private val uuidGenerator: UuidGenerator,
    private val sessionMetadataService: SessionMetadataService,
) {
    suspend fun create(fileUpload: FileUpload): UUID {
        val datalogs = fileParsingService.parse(fileUpload)

        val firstDatalogTimestamp = datalogs.minBy { it.epochMilliseconds }.epochMilliseconds
        val existingSessionId =
            sessionMetadataService.getExistingSessionId(fileUpload.metadata.user.email, firstDatalogTimestamp)

        if (existingSessionId != null) {
            throw SessionAlreadyExistsException("A session already exists for this user and timestamp")
        }

        val sessionId = uuidGenerator.generate()
        datalogRepository.saveAll(datalogs.map { it.copy(sessionId = sessionId) }).collect()

        return sessionId
    }

    suspend fun update(fileUpload: FileUpload) {
        val existingRecords = datalogRepository.findAllBySessionId(fileUpload.metadata.sessionId!!)
        val recordCount = existingRecords.count()

        if (recordCount == 0) {
            throw SessionDoesNotExistException("Session id ${fileUpload.metadata.sessionId} does not exist")
        }

        val oldDatalogs = datalogRepository.deleteBySessionId(fileUpload.metadata.sessionId)
        oldDatalogs.collect()

        val newDatalogs = fileParsingService.parse(fileUpload)

        datalogRepository.saveAll(newDatalogs).collect()
    }
}
