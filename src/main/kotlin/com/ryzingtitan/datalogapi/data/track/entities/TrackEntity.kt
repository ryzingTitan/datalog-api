package com.ryzingtitan.datalogapi.data.track.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document("track")
data class TrackEntity(
    @Id val id: String? = null,
    val trackId: UUID,
    val name: String,
    val latitude: Double,
    val longitude: Double,
)
