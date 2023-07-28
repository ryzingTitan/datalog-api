package com.ryzingtitan.datalogapi.domain.fileupload.services

import com.ryzingtitan.datalogapi.data.datalog.repositories.DatalogRepository
import com.ryzingtitan.datalogapi.domain.fileupload.dtos.FileUpload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FileParsingService(
    private val datalogRepository: DatalogRepository,
    private val rowParsingService: RowParsingService,
    private val columnConfigurationService: ColumnConfigurationService,
) {
    private val logger: Logger = LoggerFactory.getLogger(FileParsingService::class.java)

    suspend fun parse(fileUpload: FileUpload) {
        logger.info("Beginning to parse file: ${fileUpload.metadata.fileName}")

        val fileLines = mutableListOf<String>()

        fileUpload.file.map { dataBuffer ->
            val fileData = dataBuffer.asInputStream().readAllBytes().decodeToString()
            fileLines.addAll(fileData.split("\n").dropLast(1))
        }
            .flowOn(Dispatchers.IO).first()

        val columnConfiguration = columnConfigurationService.create(fileLines.first())

        val fileLinesWithoutHeader = removeHeaderRow(fileLines)

        fileLinesWithoutHeader.map { fileLine ->
            val datalog =
                rowParsingService.parse(fileLine, fileUpload.metadata, columnConfiguration)

            datalogRepository.save(datalog)
        }
        logger.info("File parsing completed for file: ${fileUpload.metadata.fileName}")
    }

    fun removeHeaderRow(fileLines: List<String>): List<String> {
        return fileLines.drop(1)
    }
}
