package com.ryzingtitan.datalogapi.presentation.controllers

import com.ryzingtitan.datalogapi.domain.sessionmetadata.dtos.SessionMetadata
import com.ryzingtitan.datalogapi.domain.sessionmetadata.services.SessionMetadataService
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:3000", "https://datalog-viewer-uonahdb5jq-uc.a.run.app"])
@RequestMapping(path = ["/api/sessions"])
class SessionMetadataController(private val sessionMetadataService: SessionMetadataService) {
    private val logger = LoggerFactory.getLogger(SessionMetadataController::class.java)

    @GetMapping("/metadata")
    fun getAllSessionMetadataByUser(
        @RequestParam username: String,
    ): Flow<SessionMetadata> {
        logger.info("Retrieving metadata for all sessions for user: $username")
        return sessionMetadataService.getAllSessionMetadataByUser(username)
    }
}
