package com.ryzingtitan.datalogapi.cucumber.repositories

import com.ryzingtitan.datalogapi.data.track.entities.TrackEntity
import com.ryzingtitan.datalogapi.data.track.repositories.TrackRepository
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.DataTableType
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import java.util.*

class TrackRepositoryStepDefs(private val trackRepository: TrackRepository) {
    @Given("the following tracks exist:")
    fun givenTheFollowingTracksExist(table: DataTable) {
        val tracks = table.tableConverter.toList<TrackEntity>(table, TrackEntity::class.java)

        runBlocking {
            trackRepository.saveAll(tracks).collect()
        }
    }

    @Then("the following tracks will exist:")
    fun thenTheFollowingTracksWillExist(table: DataTable) {
        val expectedTracks = table.tableConverter.toList<TrackEntity>(table, TrackEntity::class.java)

        val actualTracks = mutableListOf<TrackEntity>()
        runBlocking {
            trackRepository.findAll().collect { track ->
                actualTracks.add(track.copy(id = null))
            }
        }

        assertEquals(expectedTracks.sortedBy { it.name }, actualTracks.sortedBy { it.name })
    }

    @Before
    fun setup() {
        runBlocking {
            trackRepository.deleteAll()
        }
    }

    @DataTableType
    fun mapTrackEntity(tableRow: Map<String, String>): TrackEntity {
        return TrackEntity(
            trackId = UUID.fromString(tableRow["trackId"]),
            name = tableRow["name"].toString(),
            longitude = tableRow["longitude"].toString().toDouble(),
            latitude = tableRow["latitude"].toString().toDouble(),
        )
    }
}
