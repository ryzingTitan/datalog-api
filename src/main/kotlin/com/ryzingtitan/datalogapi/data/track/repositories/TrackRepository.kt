package com.ryzingtitan.datalogapi.data.track.repositories

import com.ryzingtitan.datalogapi.data.track.entities.TrackEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.UUID

interface TrackRepository : CoroutineCrudRepository<TrackEntity, String> {
    fun findByName(name: String): Flow<TrackEntity>

    fun deleteByTrackId(trackId: UUID): Flow<TrackEntity>
}
