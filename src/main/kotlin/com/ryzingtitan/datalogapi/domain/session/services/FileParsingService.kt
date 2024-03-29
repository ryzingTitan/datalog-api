package com.ryzingtitan.datalogapi.domain.session.services

import com.ryzingtitan.datalogapi.data.datalog.entities.DatalogEntity
import com.ryzingtitan.datalogapi.domain.session.dtos.FileUpload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
class FileParsingService(
    private val rowParsingService: RowParsingService,
    private val columnConfigurationService: ColumnConfigurationService,
) {
    private val logger: Logger = LoggerFactory.getLogger(FileParsingService::class.java)

    @CacheEvict(cacheNames = ["datalogs"], allEntries = true)
    suspend fun parse(fileUpload: FileUpload): List<DatalogEntity> {
        logger.info("Beginning to parse file: ${fileUpload.metadata.fileName}")

        val fileData = StringBuilder()

        fileUpload.file.map { dataBuffer ->
            fileData.append(dataBuffer.asInputStream().readAllBytes().decodeToString())
        }
            .flowOn(Dispatchers.IO).collect()

        val fileLines = fileData.toString().replace("\r", "").split("\n").dropLast(1)

        val columnConfiguration = columnConfigurationService.create(fileLines.first())

        val fileLinesWithoutHeader = removeHeaderRow(fileLines)

        val datalogs = mutableListOf<DatalogEntity>()
        fileLinesWithoutHeader.map { fileLine ->
            val datalog = rowParsingService.parse(fileLine, fileUpload.metadata, columnConfiguration)

            if (datalog != null) {
                datalogs.add(datalog)
            }
        }

        logger.info("File parsing completed for file: ${fileUpload.metadata.fileName}")

        return datalogs
    }

    private fun removeHeaderRow(fileLines: List<String>): List<String> {
        return fileLines.drop(1)
    }
}
