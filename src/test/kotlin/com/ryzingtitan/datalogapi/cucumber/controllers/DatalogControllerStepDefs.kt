package com.ryzingtitan.datalogapi.cucumber.controllers

import com.ryzingtitan.datalogapi.cucumber.common.CommonControllerStepDefs
import com.ryzingtitan.datalogapi.domain.datalog.dtos.Data
import com.ryzingtitan.datalogapi.domain.datalog.dtos.Datalog
import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
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
    @When("the datalogs for session with id {string} are retrieved")
    fun whenTheDatalogsForSessionWithIdAreRetrieved(sessionIdString: String) {
        val sessionId = UUID.fromString(sessionIdString)

        runBlocking {
            webClient.get()
                .uri("/$sessionId/datalogs")
                .accept(MediaType.APPLICATION_JSON)
                .awaitExchange { clientResponse ->
                    handleMultipleDatalogResponse(clientResponse)
                }
        }
    }

    @Then("the following datalogs are returned:")
    fun thenTheFollowingDatalogsAreReturned(table: DataTable) {
        val expectedDatalogs = table.tableConverter.toList<Datalog>(table, Datalog::class.java)

        assertEquals(expectedDatalogs, returnedDatalogs)
    }

    @Before
    fun setup() {
        webClient = WebClient.create("http://localhost:$port/api/sessions")
    }

    @DataTableType
    fun mapDatalog(tableRow: Map<String, String>): Datalog {
        return Datalog(
            sessionId = UUID.fromString(tableRow["sessionId"]),
            timestamp = Instant.parse(tableRow["timestamp"]),
            data = Data(
                longitude = tableRow["longitude"].toString().toDouble(),
                latitude = tableRow["latitude"].toString().toDouble(),
                altitude = tableRow["altitude"].toString().toFloat(),
                intakeAirTemperature = tableRow["intakeAirTemperature"].toString().toIntOrNull(),
                boostPressure = tableRow["boostPressure"].toString().toFloatOrNull(),
                coolantTemperature = tableRow["coolantTemperature"].toString().toIntOrNull(),
                engineRpm = tableRow["engineRpm"].toString().toIntOrNull(),
                speed = tableRow["speed"].toString().toIntOrNull(),
                throttlePosition = tableRow["throttlePosition"].toString().toFloatOrNull(),
                airFuelRatio = tableRow["airFuelRatio"].toString().toFloatOrNull(),
            ),
            trackInfo = TrackInfo(
                name = tableRow["trackName"].toString(),
                latitude = tableRow["trackLatitude"].toString().toDouble(),
                longitude = tableRow["trackLongitude"].toString().toDouble(),
            ),
            user = User(
                firstName = tableRow["firstName"].toString(),
                lastName = tableRow["lastName"].toString(),
                email = tableRow["email"].toString(),
            ),
        )
    }

    private suspend fun handleMultipleDatalogResponse(clientResponse: ClientResponse) {
        CommonControllerStepDefs.responseStatus = clientResponse.statusCode() as HttpStatus

        if (clientResponse.statusCode() == HttpStatus.OK) {
            val datalogs = clientResponse.awaitEntityList<Datalog>().body

            if (datalogs != null) {
                returnedDatalogs.addAll(datalogs)
            }
        }
    }

    @LocalServerPort
    private val port = 0

    private lateinit var webClient: WebClient

    private val returnedDatalogs = mutableListOf<Datalog>()
}
