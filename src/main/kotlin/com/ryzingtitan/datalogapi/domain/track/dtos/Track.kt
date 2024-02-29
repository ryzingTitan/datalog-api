package com.ryzingtitan.datalogapi.domain.track.dtos

import lombok.Generated
import java.util.UUID

@Generated
data class Track(
    val id: UUID?,
    val name: String,
    val latitude: Double,
    val longitude: Double,
)
