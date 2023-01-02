package com.ryzingtitan.datalogapi.cucumber.controllers

import com.ryzingtitan.datalogapi.cucumber.common.CommonControllerStepDefs.CommonControllerStepDefsSharedState.responseStatus
import com.ryzingtitan.datalogapi.domain.sessionmetadata.dtos.SessionMetadata
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

class SessionMetadataControllerStepDefs {
    @When("the metadata for the sessions is retrieved")
    fun whenTheMetadataForTheSessionsIsRetrieved() {
        runBlocking {
            webClient.get()
                .uri("/metadata")
                .accept(MediaType.APPLICATION_JSON)
                .awaitExchange { clientResponse ->
                    handleMultipleSessionMetadataRecordsResponse(clientResponse)
                }
        }
    }

    @Then("the following session metadata is returned:")
    fun thenTheFollowingSessionMetadataIsReturned(table: DataTable) {
        val expectedSessionMetadataRecords =
            table.tableConverter.toList<SessionMetadata>(table, SessionMetadata::class.java)

        assertEquals(
            expectedSessionMetadataRecords.sortedBy { it.startTime },
            returnedSessionMetadataRecords.sortedBy { it.startTime }
        )
    }

    @Before
    fun setup() {
        webClient = WebClient.create("http://localhost:$port/api/sessions")
    }

    @DataTableType
    fun mapSessionMetadata(tableRow: Map<String, String>): SessionMetadata {
        return SessionMetadata(
            sessionId = UUID.fromString(tableRow["sessionId"]),
            startTime = Instant.parse(tableRow["startTime"]),
            endTime = Instant.parse(tableRow["endTime"])
        )
    }

    private suspend fun handleMultipleSessionMetadataRecordsResponse(clientResponse: ClientResponse) {
        responseStatus = clientResponse.statusCode() as HttpStatus

        if (clientResponse.statusCode() == HttpStatus.OK) {
            val sessionMetadataList = clientResponse.awaitEntityList<SessionMetadata>().body

            if (sessionMetadataList != null)
                returnedSessionMetadataRecords.addAll(sessionMetadataList)
        }
    }

    @LocalServerPort
    private val port = 0

    private lateinit var webClient: WebClient

    private val returnedSessionMetadataRecords = mutableListOf<SessionMetadata>()
}
