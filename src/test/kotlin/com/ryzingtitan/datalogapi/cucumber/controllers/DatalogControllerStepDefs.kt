package com.ryzingtitan.datalogapi.cucumber.controllers

import com.ryzingtitan.datalogapi.cucumber.common.CommonControllerStepDefs.CommonControllerStepDefsSharedState.responseStatus
import com.ryzingtitan.datalogapi.domain.dtos.DatalogRecord
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitEntity
import org.springframework.web.reactive.function.client.awaitEntityList

class DatalogControllerStepDefs {
    @Before("@users")
    fun setup() {
        webClient = WebClient.create("http://localhost:$port/api/users")
    }

    @DataTableType
    fun mapUser(tableRow: Map<String, String>): DatalogRecord {
        return DatalogRecord(
            id = tableRow["id"]?.toInt() ?: 0,
            firstName = tableRow["firstName"].toString(),
            lastName = tableRow["lastName"].toString(),
            fullName = tableRow["fullName"].toString()
        )
    }

    private suspend fun handleSingleUserResponse(clientResponse: ClientResponse) {
        responseStatus = clientResponse.statusCode()

        if (clientResponse.statusCode() == HttpStatus.OK) {
            val datalogRecord = clientResponse.awaitEntity<DatalogRecord>().body

            if (datalogRecord != null)
                returnedDatalogRecords.add(datalogRecord)
        }
    }

    private suspend fun handleMultipleUserResponse(clientResponse: ClientResponse) {
        responseStatus = clientResponse.statusCode()

        if (clientResponse.statusCode() == HttpStatus.OK) {
            val datalogRecordList = clientResponse.awaitEntityList<DatalogRecord>().body

            if (datalogRecordList != null)
                returnedDatalogRecords.addAll(datalogRecordList)
        }
    }

    @LocalServerPort
    private val port = 0

    private lateinit var webClient: WebClient

    private val returnedDatalogRecords = mutableListOf<DatalogRecord>()
}
