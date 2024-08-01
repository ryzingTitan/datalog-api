package com.ryzingtitan.datalogapi.data.tracks.repositories

import com.ryzingtitan.datalogapi.data.tracks.entities.TrackEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TrackRepository : CoroutineCrudRepository<TrackEntity, Int> {
    suspend fun findByName(name: String): TrackEntity?
}
