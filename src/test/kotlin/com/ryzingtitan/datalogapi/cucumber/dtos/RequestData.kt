package com.ryzingtitan.datalogapi.cucumber.dtos

data class RequestData(
    val userEmail: String,
    val userFirstName: String,
    val userLastName: String,
    val trackId: Int,
    val carId: Int,
)
