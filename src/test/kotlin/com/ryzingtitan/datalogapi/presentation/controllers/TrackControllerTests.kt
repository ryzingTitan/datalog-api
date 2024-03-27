package com.ryzingtitan.datalogapi.presentation.controllers

import com.ryzingtitan.datalogapi.domain.track.dtos.Track
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt
import org.springframework.test.web.reactive.server.expectBodyList
import java.util.UUID

@ExperimentalCoroutinesApi
class TrackControllerTests : CommonControllerTests() {
    @Nested
    inner class GetTracks {
        @Test
        fun `returns 'OK' status with all tracks`() =
            runTest {
                whenever(mockTrackService.getAll()).thenReturn(flowOf(firstTrack, secondTrack))

                webTestClient
                    .mutateWith(mockJwt())
                    .get()
                    .uri("/api/tracks")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk
                    .expectBodyList<Track>()
                    .contains(firstTrack, secondTrack)

                verify(mockTrackService, times(1)).getAll()
            }
    }

    @Nested
    inner class CreateTrack {
        @Test
        fun `returns 'CREATED' status and creates new track`() =
            runTest {
                whenever(mockTrackService.create(firstTrack.copy(id = null))).thenReturn(firstTrackId)

                webTestClient
                    .mutateWith(mockJwt())
                    .post()
                    .uri("/api/tracks")
                    .bodyValue(firstTrack.copy(id = null))
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isCreated
                    .expectHeader()
                    .location("/api/tracks/$firstTrackId")

                verify(mockTrackService, times(1)).create(firstTrack.copy(id = null))
            }
    }

    @Nested
    inner class UpdateTrack {
        @Test
        fun `returns 'OK' status and updates track`() =
            runTest {
                webTestClient
                    .mutateWith(mockJwt())
                    .put()
                    .uri("/api/tracks/$firstTrackId")
                    .bodyValue(firstTrack)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk

                verify(mockTrackService, times(1)).update(firstTrack)
            }
    }

    @Nested
    inner class DeleteTrack {
        @Test
        fun `returns 'OK' status and deletes track`() =
            runTest {
                whenever(mockTrackService.delete(firstTrackId)).thenReturn(flowOf(firstTrack))

                webTestClient
                    .mutateWith(mockJwt())
                    .delete()
                    .uri("/api/tracks/$firstTrackId")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk
                    .expectBodyList<Track>()
                    .contains(firstTrack)

                verify(mockTrackService, times(1)).delete(firstTrackId)
            }
    }

    private val firstTrackId = UUID.randomUUID()
    private val secondTrackId = UUID.randomUUID()

    private val firstTrack =
        Track(
            id = firstTrackId,
            name = FIRST_TRACK_NAME,
            latitude = FIRST_TRACK_LATITUDE,
            longitude = FIRST_TRACK_LONGITUDE,
        )

    private val secondTrack =
        Track(
            id = secondTrackId,
            name = SECOND_TRACK_NAME,
            latitude = SECOND_TRACK_LATITUDE,
            longitude = SECOND_TRACK_LONGITUDE,
        )

    companion object TrackControllerTestConstants {
        const val FIRST_TRACK_NAME = "Test Track 1"
        const val FIRST_TRACK_LATITUDE = 12.0
        const val FIRST_TRACK_LONGITUDE = 14.0

        const val SECOND_TRACK_NAME = "Test Track 2"
        const val SECOND_TRACK_LATITUDE = 30.0
        const val SECOND_TRACK_LONGITUDE = 33.0
    }
}
