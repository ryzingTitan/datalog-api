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
import org.springframework.http.HttpStatus
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.awaitExchange
import java.util.*

class FileUploadControllerStepDefs {
    @When("the file is uploaded for a session with the following data:")
    fun theFileIsUploadedForSessionWithIdAndTheFollowingData(table: DataTable) {
        val requestData = table.tableConverter.toList<RequestData>(table, RequestData::class.java)

        val multipartBodyBuilder = MultipartBodyBuilder()
        multipartBodyBuilder.part("user", requestData.first().user)
        multipartBodyBuilder.part("trackInfo", requestData.first().trackInfo)
        multipartBodyBuilder.part("uploadFile", FileSystemResource("testFiles/testFile.txt"))
        val multiPartData = multipartBodyBuilder.build()

        runBlocking {
            CommonControllerStepDefs.webClient
                .put()
                .body(BodyInserters.fromMultipartData(multiPartData))
                .header(
                    "Authorization",
                    "Bearer ${CommonControllerStepDefs.authorizationToken?.serialize()}",
                )
                .awaitExchange { clientResponse ->
                    handleUploadFileResponse(clientResponse)
                }
        }
    }

    private fun handleUploadFileResponse(clientResponse: ClientResponse) {
        CommonControllerStepDefs.responseStatus = clientResponse.statusCode() as HttpStatus
    }

    @DataTableType
    fun mapRequestData(tableRow: Map<String, String>): RequestData {
        return RequestData(
            trackInfo = TrackInfo(
                name = tableRow["trackName"].toString(),
                latitude = tableRow["latitude"].toString().toDouble(),
                longitude = tableRow["longitude"].toString().toDouble(),
            ),
            user = User(
                firstName = tableRow["firstName"].toString(),
                lastName = tableRow["lastName"].toString(),
                email = tableRow["email"].toString(),
            ),
        )
    }
}
