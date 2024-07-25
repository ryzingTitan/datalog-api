package com.ryzingtitan.datalogapi.presentation.controllers

import com.ryzingtitan.datalogapi.domain.sessions.dtos.FileUpload
import com.ryzingtitan.datalogapi.domain.sessions.dtos.FileUploadMetadata
import com.ryzingtitan.datalogapi.domain.sessions.dtos.Session
import com.ryzingtitan.datalogapi.domain.sessions.services.SessionService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RestController
@RequestMapping(path = ["/api/sessions"])
class SessionController(
    private val sessionService: SessionService,
) {
    @GetMapping
    suspend fun getAllSessionsByUser(
        @RequestParam userEmail: String,
    ): Flow<Session> {
        logger.info("Retrieving all sessions for user: $userEmail")
        return sessionService.getAllByUser(userEmail)
    }

    @PostMapping
    @Suppress("LongParameterList")
    suspend fun createSession(
        @RequestPart(name = "userEmail") userEmail: String,
        @RequestPart(name = "userFirstName") userFirstName: String,
        @RequestPart(name = "userLastName") userLastName: String,
        @RequestPart(name = "trackId") trackId: Int,
        @RequestPart(name = "uploadFile") uploadFile: FilePart,
        response: ServerHttpResponse,
        exchange: ServerWebExchange,
    ) {
        val fileUpload =
            FileUpload(
                file = uploadFile.content().asFlow(),
                metadata =
                    FileUploadMetadata(
                        fileName = uploadFile.filename(),
                        sessionId = null,
                        trackId = trackId,
                        userEmail = userEmail,
                        userFirstName = userFirstName,
                        userLastName = userLastName,
                    ),
            )
        val sessionId = sessionService.create(fileUpload)
        response.headers.add(
            HttpHeaders.LOCATION,
            "${exchange.request.uri}/$sessionId",
        )
        response.statusCode = HttpStatus.CREATED
    }

    @PutMapping("/{sessionId}")
    @Suppress("LongParameterList")
    suspend fun updateSession(
        @RequestPart(name = "userEmail") userEmail: String,
        @RequestPart(name = "userFirstName") userFirstName: String,
        @RequestPart(name = "userLastName") userLastName: String,
        @RequestPart(name = "trackId") trackId: Int,
        @RequestPart(name = "uploadFile") uploadFile: FilePart,
        @PathVariable(name = "sessionId") sessionId: Int,
    ) {
        val fileUpload =
            FileUpload(
                file = uploadFile.content().asFlow(),
                metadata =
                    FileUploadMetadata(
                        fileName = uploadFile.filename(),
                        sessionId = sessionId,
                        trackId = trackId,
                        userEmail = userEmail,
                        userFirstName = userFirstName,
                        userLastName = userLastName,
                    ),
            )
        sessionService.update(fileUpload)
    }

    private val logger = LoggerFactory.getLogger(SessionController::class.java)
}
