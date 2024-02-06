package com.example.musicmetadata.adapter.driver.job

import com.example.musicmetadata.port.driven.ArtistRepository
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ArtistOfTheDayJobTest {

    @MockK
    private lateinit var artistRepository: ArtistRepository

    @InjectMockKs
    private lateinit var artistOfTheDayJob: ArtistOfTheDayJob

    @Test
    fun `updates the artist of the day`() {
        // given
        every { artistRepository.updateArtistOfTheDay() } just Runs

        // when
        artistOfTheDayJob.updateArtistOfTheDay()

        // then
        verify { artistRepository.updateArtistOfTheDay() }
    }
}