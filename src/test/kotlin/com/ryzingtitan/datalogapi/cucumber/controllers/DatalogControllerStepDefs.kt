package com.ryzingtitan.datalogapi.cucumber.controllers

import com.ryzingtitan.datalogapi.cucumber.common.CommonControllerStepDefs
import com.ryzingtitan.datalogapi.domain.datalogrecord.dtos.DatalogRecord
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitEntityList
import org.springframework.web.reactive.function.client.awaitExchange
import java.time.Instant
import java.util.*

class DatalogControllerStepDefs {
    @When("the datalog records for session with id {string} are retrieved")
    fun whenTheDatalogRecordsForSessionWithIdAreRetrieved(sessionIdString: String) {
        val sessionId = UUID.fromString(sessionIdString)

        runBlocking {
            webClient.get()
                .uri("/$sessionId/datalogs")
                .accept(MediaType.APPLICATION_JSON)
                .awaitExchange { clientResponse ->
                    handleMultipleDatalogRecordsResponse(clientResponse)
                }
        }
    }

    @Then("the following data log records are returned:")
    fun thenTheFollowingDatalogRecordsAreReturned(table: DataTable) {
        val expectedDatalogRecords = table.tableConverter.toList<DatalogRecord>(table, DatalogRecord::class.java)

        assertEquals(expectedDatalogRecords, returnedDatalogRecords)
    }

    @Before
    fun setup() {
        webClient = WebClient.create("http://localhost:$port/api/sessions")
    }

    @DataTableType
    fun mapDatalogRecord(tableRow: Map<String, String>): DatalogRecord {
        return DatalogRecord(
            sessionId = UUID.fromString(tableRow["sessionId"]),
            timestamp = Instant.parse(tableRow["timestamp"]),
            intakeAirTemperature = tableRow["intakeAirTemperature"].toString().toIntOrNull(),
            boostPressure = tableRow["boostPressure"].toString().toFloatOrNull(),
            coolantTemperature = tableRow["coolantTemperature"].toString().toIntOrNull(),
            engineRpm = tableRow["engineRpm"].toString().toIntOrNull(),
            speed = tableRow["speed"].toString().toIntOrNull(),
            throttlePosition = tableRow["throttlePosition"].toString().toFloatOrNull(),
        )
    }

    private suspend fun handleMultipleDatalogRecordsResponse(clientResponse: ClientResponse) {
        CommonControllerStepDefs.responseStatus = clientResponse.statusCode() as HttpStatus

        if (clientResponse.statusCode() == HttpStatus.OK) {
            val datalogRecordList = clientResponse.awaitEntityList<DatalogRecord>().body

            if (datalogRecordList != null) {
                returnedDatalogRecords.addAll(datalogRecordList)
            }
        }
    }

    @LocalServerPort
    private val port = 0

    private lateinit var webClient: WebClient

    private val returnedDatalogRecords = mutableListOf<DatalogRecord>()
}
