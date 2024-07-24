package com.ryzingtitan.datalogapi.domain.track.dtos

import java.util.UUID

data class Track(
    val id: UUID?,
    val name: String,
    val latitude: Double,
    val longitude: Double,
)
