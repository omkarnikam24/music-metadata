package com.example.musicmetadata.adapter.driven.db

import com.example.musicmetadata.IntegrationTest
import com.example.musicmetadata.adapter.driven.db.exception.DatabaseException
import com.example.musicmetadata.data.someArtist
import com.example.musicmetadata.port.driven.ArtistLastFeaturedOnCommand
import com.example.musicmetadata.port.driven.ArtistNameCommand
import com.example.musicmetadata.port.driven.ArtistRepository
import org.generated.jooq.tables.references.ARTIST
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate

@IntegrationTest
class ArtistJooqRepositoryIntegrationTest(
    private val jooq: DSLContext,
    private val artistRepository: ArtistRepository,
) {
    @BeforeEach
    fun init() {
        jooq.truncate(ARTIST).cascade().execute()
    }

    @Test
    fun `saves an artist`() {
        // given
        val artist = someArtist()

        // when
        val artistId = artistRepository.save(artist)

        // then
        val actualArtist = jooq.selectFrom(ARTIST).where(ARTIST.ID.eq(artistId)).fetchOne()

        assertEquals(artist.name, actualArtist!!.name)
        assertNull(actualArtist.lastFeaturedOn)
    }

    @Test
    fun `throws an exception when artist name exists`() {
        // given
        val artist = someArtist()
        artistRepository.save(artist)

        // when - then
        val actualThrown =
            assertThrows<DatabaseException> {
                artistRepository.save(artist)
            }

        assertEquals(actualThrown.message, "Failure while saving an Artist")
    }

    @Test
    fun `updates name of an artist`() {
        // given
        val artist = someArtist()
        val newArtistName = "The Weeknd"
        val savedArtistId = artistRepository.save(artist)

        // when
        artistRepository.update(savedArtistId, ArtistNameCommand(newArtistName))

        // then
        val updatedArtist = jooq.selectFrom(ARTIST).where(ARTIST.ID.eq(savedArtistId)).fetchOne()

        assertEquals(updatedArtist!!.name, newArtistName)
    }

    @Test
    fun `updates last featured on date of an artist`() {
        // given
        val artist = someArtist()
        val newLastFeaturedOn = artist.lastFeaturedOn!!.plusDays(1)
        val savedArtistId = artistRepository.save(artist)

        // when
        artistRepository.update(savedArtistId, ArtistLastFeaturedOnCommand(newLastFeaturedOn))

        // then
        val updatedArtist = jooq.selectFrom(ARTIST).where(ARTIST.ID.eq(savedArtistId)).fetchOne()

        assertEquals(updatedArtist!!.lastFeaturedOn, newLastFeaturedOn)
    }

    @Test
    fun `gets an artist of the day`() {
        // given
        val artist = someArtist()
        val today = LocalDate.now()

        val savedArtistId = artistRepository.save(artist)
        artistRepository.update(savedArtistId, ArtistLastFeaturedOnCommand(today))

        // when
        val artistOfTheDay = artistRepository.getArtistOfTheDay()

        // then
        assertEquals(artistOfTheDay, artist)
    }

    @Test
    fun `throws exception if there's no artist of the day`() {
        // given
        val artist = someArtist()
        artistRepository.save(artist)

        // when
        val actualThrown = assertThrows<DatabaseException> {
            artistRepository.getArtistOfTheDay()
        }

        // then
        assertEquals(actualThrown.message, "No Artist of the day found for date: ${LocalDate.now()}")
    }

    @Test
    fun `updates oldest artist in the rotation as an artist of the day`() {
        // given
        val yesterday = LocalDate.now().minusDays(1)
        val firstArtist = someArtist(name = "Artist 1")
        val secondArtist = someArtist(name = "Artist 2")
        val thirdArtist = someArtist(name = "Artist 3")

        val firstArtistId = artistRepository.save(firstArtist)
        artistRepository.save(secondArtist)
        artistRepository.save(thirdArtist)

        artistRepository.update(firstArtistId, ArtistLastFeaturedOnCommand(yesterday))

        // when
        artistRepository.updateArtistOfTheDay()

        // then
        val artistOfTheDay = artistRepository.getArtistOfTheDay()
        assertEquals(artistOfTheDay, secondArtist)
    }
}
