package com.ryzingtitan.datalogapi.cucumber.dtos

import com.ryzingtitan.datalogapi.domain.datalog.dtos.TrackInfo
import com.ryzingtitan.datalogapi.domain.datalog.dtos.User

data class RequestData(
    val user: User,
    val trackInfo: TrackInfo,
)
