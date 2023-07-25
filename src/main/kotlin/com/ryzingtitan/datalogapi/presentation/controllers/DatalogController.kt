package com.ryzingtitan.datalogapi.presentation.controllers

import com.ryzingtitan.datalogapi.domain.datalog.dtos.Datalog
import com.ryzingtitan.datalogapi.domain.datalog.services.DatalogService
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@CrossOrigin(origins = ["http://localhost:3000", "https://datalog-viewer-uonahdb5jq-uc.a.run.app"])
@RequestMapping(path = ["/api/sessions/"])
class DatalogController(private val datalogService: DatalogService) {
    private val logger = LoggerFactory.getLogger(DatalogController::class.java)

    @GetMapping("/{sessionId}/datalogs")
    fun getDatalogsBySessionId(@PathVariable(name = "sessionId") sessionId: UUID): Flow<Datalog> {
        logger.info("Retrieving datalog records for session id: $sessionId")
        return datalogService.getAllBySessionId(sessionId)
    }
}
