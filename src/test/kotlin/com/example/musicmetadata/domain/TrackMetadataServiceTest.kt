package com.example.musicmetadata.domain

import com.example.musicmetadata.data.someTrack
import com.example.musicmetadata.port.driven.TrackRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class TrackMetadataServiceTest {

    @InjectMockKs
    private lateinit var trackMetadataService: TrackMetadataService

    @MockK
    private lateinit var trackRepository: TrackRepository

    @Test
    fun `saves a track`() {
        // given
        val track = someTrack()
        every { trackRepository.save(track) } returns track.id

        // when
        val actualTrackId = trackMetadataService.save(track)

        // then
        Assertions.assertEquals(track.id, actualTrackId)
    }

    @Test
    fun `gets tracks for an artist`() {
        // given
        val artistId = UUID.randomUUID()
        val track = someTrack()
        val expectedTracks = listOf(track)
        every { trackRepository.getTracks(artistId) } returns expectedTracks

        // when
        val actualTracks = trackMetadataService.getTracks(artistId)

        // then
        Assertions.assertEquals(expectedTracks, actualTracks)
    }
}