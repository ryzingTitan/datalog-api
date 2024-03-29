package com.ryzingtitan.datalogapi.cucumber.controllers

import com.ryzingtitan.datalogapi.cucumber.common.CommonControllerStepDefs
import com.ryzingtitan.datalogapi.cucumber.dtos.RequestData
import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User
import io.cucumber.datatable.DataTable
import io.cucumber.java.DataTableType
import io.cucumber.java.en.When
import kotlinx.coroutines.runBlocking
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.awaitExchange
import java.util.UUID

class SessionControllerStepDefs {
    @When("the file is uploaded for a session with the following data:")
    fun theFileIsUploadedForSessionWithTheFollowingData(table: DataTable) {
        val requestData = table.tableConverter.toList<RequestData>(table, RequestData::class.java)

        val multipartBodyBuilder = MultipartBodyBuilder()
        multipartBodyBuilder.part("userEmail", requestData.first().user.email)
        multipartBodyBuilder.part("userLastName", requestData.first().user.lastName)
        multipartBodyBuilder.part("userFirstName", requestData.first().user.firstName)
        multipartBodyBuilder.part("trackName", requestData.first().trackInfo.name)
        multipartBodyBuilder.part("trackLatitude", requestData.first().trackInfo.latitude)
        multipartBodyBuilder.part("trackLongitude", requestData.first().trackInfo.longitude)
        multipartBodyBuilder.part("uploadFile", FileSystemResource("testFiles/testFile.txt"))
        val multiPartData = multipartBodyBuilder.build()

        runBlocking {
            CommonControllerStepDefs.webClient
                .post()
                .uri("/sessions")
                .body(BodyInserters.fromMultipartData(multiPartData))
                .header(
                    "Authorization",
                    "Bearer ${CommonControllerStepDefs.authorizationToken?.serialize()}",
                )
                .awaitExchange { clientResponse ->
                    handleSessionResponse(clientResponse)
                }
        }
    }

    @When("the file is uploaded for a session with the following data and session id {string}:")
    fun theFileIsUploadedForSessionWithTheFollowingDataAndSessionId(
        sessionIdString: String,
        table: DataTable,
    ) {
        val requestData = table.tableConverter.toList<RequestData>(table, RequestData::class.java)
        val sessionId = UUID.fromString(sessionIdString)

        val multipartBodyBuilder = MultipartBodyBuilder()
        multipartBodyBuilder.part("userEmail", requestData.first().user.email)
        multipartBodyBuilder.part("userLastName", requestData.first().user.lastName)
        multipartBodyBuilder.part("userFirstName", requestData.first().user.firstName)
        multipartBodyBuilder.part("trackName", requestData.first().trackInfo.name)
        multipartBodyBuilder.part("trackLatitude", requestData.first().trackInfo.latitude)
        multipartBodyBuilder.part("trackLongitude", requestData.first().trackInfo.longitude)
        multipartBodyBuilder.part("uploadFile", FileSystemResource("testFiles/testFile.txt"))
        val multiPartData = multipartBodyBuilder.build()

        runBlocking {
            CommonControllerStepDefs.webClient
                .put()
                .uri("/sessions/$sessionId")
                .body(BodyInserters.fromMultipartData(multiPartData))
                .header(
                    "Authorization",
                    "Bearer ${CommonControllerStepDefs.authorizationToken?.serialize()}",
                )
                .awaitExchange { clientResponse ->
                    handleSessionResponse(clientResponse)
                }
        }
    }

    private fun handleSessionResponse(clientResponse: ClientResponse) {
        CommonControllerStepDefs.responseStatus = clientResponse.statusCode() as HttpStatus
        CommonControllerStepDefs.locationHeader =
            clientResponse.headers().header(HttpHeaders.LOCATION).firstOrNull() ?: ""
    }

    @DataTableType
    fun mapRequestData(tableRow: Map<String, String>): RequestData {
        return RequestData(
            trackInfo =
                TrackInfo(
                    name = tableRow["trackName"].toString(),
                    latitude = tableRow["latitude"].toString().toDouble(),
                    longitude = tableRow["longitude"].toString().toDouble(),
                ),
            user =
                User(
                    firstName = tableRow["firstName"].toString(),
                    lastName = tableRow["lastName"].toString(),
                    email = tableRow["email"].toString(),
                ),
        )
    }
}
