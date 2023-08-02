package com.ryzingtitan.datalogapi.presentation.controllers

import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
import com.ryzingtitan.datalogapi.domain.fileupload.dtos.FileUpload
import com.ryzingtitan.datalogapi.domain.fileupload.dtos.FileUploadMetadata
import com.ryzingtitan.datalogapi.domain.fileupload.services.FileParsingService
import com.ryzingtitan.datalogapi.presentation.uuid.UuidGenerator
import kotlinx.coroutines.reactive.asFlow
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@CrossOrigin(origins = ["http://localhost:3000", "https://datalog-viewer-uonahdb5jq-uc.a.run.app"])
@RequestMapping(path = ["/api/sessions"])
class FileUploadController(
    private val fileParsingService: FileParsingService,
    private val uuidGenerator: UuidGenerator,
) {
    @PutMapping
    suspend fun upload(
        @RequestPart(name = "user") user: User,
        @RequestPart(name = "trackInfo") trackInfo: TrackInfo,
        @RequestPart(name = "uploadFile") uploadFile: FilePart,
    ) {
        val fileUpload = FileUpload(
            file = uploadFile.content().asFlow(),
            metadata = FileUploadMetadata(
                fileName = uploadFile.filename(),
                sessionId = uuidGenerator.generate(),
                trackInfo = trackInfo,
                user = user,
            ),
        )
        fileParsingService.parse(fileUpload)
    }
}
