package com.ryzingtitan.datalogapi.presentation.controllers

import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
import com.ryzingtitan.datalogapi.domain.session.dtos.FileUpload
import com.ryzingtitan.datalogapi.domain.session.dtos.FileUploadMetadata
import com.ryzingtitan.datalogapi.domain.session.services.SessionService
import kotlinx.coroutines.reactive.asFlow
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.codec.multipart.FilePart
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import java.util.UUID

@RestController
@CrossOrigin(origins = ["http://localhost:3000", "https://datalog-viewer-uonahdb5jq-uc.a.run.app"])
@RequestMapping(path = ["/api/sessions"])
class SessionController(
    private val sessionService: SessionService,
) {
    @PostMapping
    @Suppress("LongParameterList")
    suspend fun createSession(
        @RequestPart(name = "userEmail") userEmail: String,
        @RequestPart(name = "userFirstName") userFirstName: String,
        @RequestPart(name = "userLastName") userLastName: String,
        @RequestPart(name = "trackName") trackName: String,
        @RequestPart(name = "trackLongitude") trackLongitude: String,
        @RequestPart(name = "trackLatitude") trackLatitude: String,
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
                        trackInfo = TrackInfo(trackName, trackLatitude.toDouble(), trackLongitude.toDouble()),
                        user = User(userFirstName, userLastName, userEmail),
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
        @RequestPart(name = "trackName") trackName: String,
        @RequestPart(name = "trackLongitude") trackLongitude: String,
        @RequestPart(name = "trackLatitude") trackLatitude: String,
        @RequestPart(name = "uploadFile") uploadFile: FilePart,
        @PathVariable(name = "sessionId") sessionId: UUID,
    ) {
        val fileUpload =
            FileUpload(
                file = uploadFile.content().asFlow(),
                metadata =
                    FileUploadMetadata(
                        fileName = uploadFile.filename(),
                        sessionId = sessionId,
                        trackInfo = TrackInfo(trackName, trackLatitude.toDouble(), trackLongitude.toDouble()),
                        user = User(userFirstName, userLastName, userEmail),
                    ),
            )
        sessionService.update(fileUpload)
    }
}
