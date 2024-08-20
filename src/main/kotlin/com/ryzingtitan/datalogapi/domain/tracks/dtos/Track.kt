package com.ryzingtitan.datalogapi.domain.tracks.dtos

data class Track(
    val id: Int?,
    val name: String,
    val latitude: Double,
    val longitude: Double,
)
