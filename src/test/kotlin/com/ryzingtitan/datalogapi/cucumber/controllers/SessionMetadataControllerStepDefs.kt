package com.ryzingtitan.datalogapi.cucumber.controllers

import com.ryzingtitan.datalogapi.cucumber.common.CommonControllerStepDefs
import com.ryzingtitan.datalogapi.cucumber.common.CommonControllerStepDefs.CommonControllerStepDefsSharedState.responseStatus
import com.ryzingtitan.datalogapi.domain.sessionmetadata.dtos.SessionMetadata
import io.cucumber.datatable.DataTable
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.awaitEntityList
import org.springframework.web.reactive.function.client.awaitExchange
import java.time.Instant
import java.util.*

class SessionMetadataControllerStepDefs {
    @When("the metadata for the sessions is retrieved for user {string}")
    fun whenTheMetadataForTheSessionsIsRetrievedForUser(username: String) {
        runBlocking {
            CommonControllerStepDefs.webClient.get()
                .uri("/metadata?username=$username")
                .accept(MediaType.APPLICATION_JSON)
                .header(
                    "Authorization",
                    "Bearer ${CommonControllerStepDefs.authorizationToken?.serialize()}",
                )
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
            returnedSessionMetadataRecords.sortedBy { it.startTime },
        )
    }

    @DataTableType
    fun mapSessionMetadata(tableRow: Map<String, String>): SessionMetadata {
        return SessionMetadata(
            sessionId = UUID.fromString(tableRow["sessionId"]),
            startTime = Instant.parse(tableRow["startTime"]),
            endTime = Instant.parse(tableRow["endTime"]),
        )
    }

    private suspend fun handleMultipleSessionMetadataRecordsResponse(clientResponse: ClientResponse) {
        responseStatus = clientResponse.statusCode() as HttpStatus

        if (clientResponse.statusCode() == HttpStatus.OK) {
            val sessionMetadataList = clientResponse.awaitEntityList<SessionMetadata>().body

            if (sessionMetadataList != null) {
                returnedSessionMetadataRecords.addAll(sessionMetadataList)
            }
        }
    }

    private val returnedSessionMetadataRecords = mutableListOf<SessionMetadata>()
}
