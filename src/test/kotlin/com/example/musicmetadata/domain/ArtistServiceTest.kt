package com.example.musicmetadata.domain

import com.example.musicmetadata.data.someArtist
import com.example.musicmetadata.port.driven.AliasRepository
import com.example.musicmetadata.port.driven.ArtistLastFeaturedOnCommand
import com.example.musicmetadata.port.driven.ArtistNameCommand
import com.example.musicmetadata.port.driven.ArtistRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.*


@ExtendWith(MockKExtension::class)
class ArtistServiceTest {

    @InjectMockKs
    private lateinit var artistService: ArtistService

    @MockK
    private lateinit var artistRepository: ArtistRepository

    @MockK
    private lateinit var aliasRepository: AliasRepository

    @Test
    fun `updates artist name`() {
        // given
        val artistId = UUID.randomUUID()
        val artistName = "New Artist"
        val aliasId = UUID.randomUUID()

        every { artistRepository.update(artistId, ArtistNameCommand(artistName)) } just Runs
        every { aliasRepository.save(artistId, artistName) } returns aliasId

        // when
        artistService.updateArtistName(artistName = artistName, artistId = artistId)

        // then
        verify { artistRepository.update(artistId, ArtistNameCommand(artistName)) }
        verify { aliasRepository.save(artistId, artistName) }
    }

    @Test
    fun `updates artist last featured on date`() {
        // given
        val artistId = UUID.randomUUID()
        val artistLastFeaturedOn = LocalDate.now()

        every { artistRepository.update(artistId, ArtistLastFeaturedOnCommand(artistLastFeaturedOn)) } just Runs

        // when
        artistService.updateArtistLastFeaturedOn(lastFeaturedOn = artistLastFeaturedOn, artistId = artistId)

        // then
        verify { artistRepository.update(artistId, ArtistLastFeaturedOnCommand(artistLastFeaturedOn)) }
    }

    @Test
    fun `gets artist of the day`() {
        // given
        val artist = someArtist()

        every { artistRepository.getArtistOfTheDay() } returns artist

        // when
        val actual = artistService.getArtistOfTheDay()

        // then
        verify { artistRepository.getArtistOfTheDay() }
        assertEquals(artist, actual)
    }

}