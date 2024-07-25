package com.ryzingtitan.datalogapi.data.tracks.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("tracks")
data class TrackEntity(
    @Id
    val id: Int? = null,
    val name: String,
    val latitude: Double,
    val longitude: Double,
)
