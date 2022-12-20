package com.ryzingtitan.datalogapi.presentation.controllers

import com.ryzingtitan.datalogapi.domain.datalogrecord.dtos.DatalogRecord
import com.ryzingtitan.datalogapi.domain.datalogrecord.services.DatalogRecordService
import com.ryzingtitan.datalogapi.domain.sessionmetadata.dtos.SessionMetadata
import com.ryzingtitan.datalogapi.domain.sessionmetadata.services.SessionMetadataService
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(path = ["/api/datalogs/sessions"])
class SessionController(
    private val datalogRecordService: DatalogRecordService,
    private val sessionMetadataService: SessionMetadataService
) {
    private val logger = LoggerFactory.getLogger(SessionController::class.java)

    @GetMapping("/{sessionId}")
    fun getSessionById(@PathVariable(name = "sessionId") sessionId: UUID): Flow<DatalogRecord> {
        logger.info("Retrieving datalog records for session id: $sessionId")
        return datalogRecordService.getAllBySessionId(sessionId)
    }

    @GetMapping("/metadata")
    fun getAllSessionMetadata(): Flow<SessionMetadata> {
        logger.info("Retrieving metadata for all sessions")
        return sessionMetadataService.getAllSessionMetadata()
    }
}
