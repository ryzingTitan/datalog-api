package com.ryzingtitan.datalogapi.cucumber.repositories

import com.ryzingtitan.datalogapi.data.datalogrecord.entities.DatalogRecordEntity
import com.ryzingtitan.datalogapi.data.datalogrecord.repositories.DatalogRecordRepository
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.util.UUID

class DatalogRecordRepositoryStepDefs(
    private val datalogRecordRepository: DatalogRecordRepository,
) {

    @Given("the following datalog records exist:")
    fun givenTheFollowingDatalogRecordsExist(table: DataTable) {
        val datalogRecordEntities =
            table.tableConverter.toList<DatalogRecordEntity>(table, DatalogRecordEntity::class.java)

        datalogRecordEntities.forEach { datalogRecord ->
            runBlocking {
                datalogRecordRepository.save(datalogRecord)
            }
        }
    }

    @Before
    fun setup() {
        runBlocking {
            datalogRecordRepository.deleteAll()
        }
    }

    @DataTableType
    fun mapDatalogRecordEntity(tableRow: Map<String, String>): DatalogRecordEntity {
        return DatalogRecordEntity(
            sessionId = UUID.fromString(tableRow["sessionId"]),
            timestamp = Instant.parse(tableRow["timestamp"]),
            longitude = tableRow["longitude"].toString().toDouble(),
            latitude = tableRow["latitude"].toString().toDouble(),
            altitude = tableRow["altitude"].toString().toFloat(),
            intakeAirTemperature = tableRow["intakeAirTemperature"].toString().toIntOrNull(),
            boostPressure = tableRow["boostPressure"].toString().toFloatOrNull(),
            coolantTemperature = tableRow["coolantTemperature"].toString().toIntOrNull(),
            engineRpm = tableRow["engineRpm"].toString().toIntOrNull(),
            speed = tableRow["speed"].toString().toIntOrNull(),
            throttlePosition = tableRow["throttlePosition"].toString().toFloatOrNull(),
        )
    }
}
