package com.ryzingtitan.datalogapi.cucumber.repositories

import com.ryzingtitan.datalogapi.data.datalogrecord.entities.DatalogRecordEntity
import com.ryzingtitan.datalogapi.data.datalogrecord.repositories.DatalogRecordRepository
import io.cucumber.java.DataTableType
import org.springframework.r2dbc.core.DatabaseClient

class DatalogRepositoryStepDefs(
    private val datalogRecordRepository: DatalogRecordRepository,
    private val databaseClient: DatabaseClient
) {

    @DataTableType
    fun mapUserEntity(tableRow: Map<String, String>): DatalogRecordEntity {
        return DatalogRecordEntity(
            id = null,
            firstName = tableRow["firstName"].toString(),
            lastName = tableRow["lastName"].toString(),
            username = tableRow["username"].toString()
        )
    }
}
