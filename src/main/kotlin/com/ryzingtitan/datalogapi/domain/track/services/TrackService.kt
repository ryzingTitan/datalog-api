package com.ryzingtitan.datalogapi.domain.track.services

import com.ryzingtitan.datalogapi.data.track.entities.TrackEntity
import com.ryzingtitan.datalogapi.data.track.repositories.TrackRepository
import com.ryzingtitan.datalogapi.domain.track.dtos.Track
import com.ryzingtitan.datalogapi.domain.track.exceptions.TrackAlreadyExistsException
import com.ryzingtitan.datalogapi.domain.track.exceptions.TrackDoesNotExistException
import com.ryzingtitan.datalogapi.domain.uuid.UuidGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TrackService(
    private val trackRepository: TrackRepository,
    private val uuidGenerator: UuidGenerator,
) {
    private val logger: Logger = LoggerFactory.getLogger(TrackService::class.java)

    suspend fun create(track: Track): UUID {
        val existingTrack = trackRepository.findByName(track.name).firstOrNull()

        if (existingTrack != null) {
            val message = "A track already exists named ${track.name}"
            logger.error(message)
            throw TrackAlreadyExistsException(message)
        }

        val trackId = track.id ?: uuidGenerator.generate()

        trackRepository.save(
            TrackEntity(
                trackId = trackId,
                name = track.name,
                longitude = track.longitude,
                latitude = track.latitude,
            ),
        )

        logger.info("Created track named ${track.name}")

        return trackId
    }

    suspend fun update(track: Track) {
        val existingTrack = trackRepository.findByName(track.name).firstOrNull()

        if (existingTrack == null) {
            val message = "A track named ${track.name} does not exist"
            logger.error(message)
            throw TrackDoesNotExistException(message)
        }

        trackRepository.deleteByTrackId(existingTrack.trackId).collect()

        trackRepository.save(
            TrackEntity(
                trackId = track.id!!,
                name = track.name,
                longitude = track.longitude,
                latitude = track.latitude,
            ),
        )

        logger.info("Updated track named ${track.name}")
    }

    fun getAll(): Flow<Track> {
        logger.info("Retrieving all tracks")

        return trackRepository.findAll().map { trackEntity ->
            Track(
                id = trackEntity.trackId,
                name = trackEntity.name,
                longitude = trackEntity.longitude,
                latitude = trackEntity.latitude,
            )
        }
    }

    fun delete(trackId: UUID): Flow<Track> {
        val deletedTrack =
            trackRepository.deleteByTrackId(trackId).map { trackEntity ->
                Track(
                    id = trackEntity.trackId,
                    name = trackEntity.name,
                    longitude = trackEntity.longitude,
                    latitude = trackEntity.latitude,
                )
            }

        logger.info("Deleted track with id $trackId")

        return deletedTrack
    }
}
