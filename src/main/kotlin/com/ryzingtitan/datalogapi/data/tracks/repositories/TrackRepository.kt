package com.ryzingtitan.datalogapi.data.tracks.repositories

import com.ryzingtitan.datalogapi.data.tracks.entities.TrackEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface TrackRepository : CoroutineCrudRepository<TrackEntity, Int> {
    fun findByName(name: String): Flow<TrackEntity>
}
