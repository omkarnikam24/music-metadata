package com.example.musicmetadata.adapter.driven.db

import com.example.musicmetadata.IntegrationTest
import com.example.musicmetadata.adapter.driven.db.exception.DatabaseException
import com.example.musicmetadata.data.someArtist
import com.example.musicmetadata.data.someTrack
import com.example.musicmetadata.port.driven.ArtistRepository
import com.example.musicmetadata.port.driven.TrackRepository
import org.generated.jooq.tables.references.ARTIST
import org.generated.jooq.tables.references.TRACK
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@IntegrationTest
class TrackJooqRepositoryIntegrationTest(
    private val jooq: DSLContext,
    private val trackRepository: TrackRepository,
    private val artistRepository: ArtistRepository,
) {
    @BeforeEach
    fun init() {
        jooq.truncate(ARTIST).cascade().execute()
    }

    @Test
    fun `saves a track for a given artist`() {
        // given
        val artistId = artistRepository.save(someArtist())

        val someTrack = someTrack(artistId = artistId)

        println("Artist Id: $artistId")

        // when
        val savedTrackId = trackRepository.save(someTrack)

        // then
        val actualTrack = jooq.selectFrom(TRACK).where(TRACK.ID.eq(savedTrackId)).fetchOne()

        assertEquals(someTrack.artistId, actualTrack!!.artistId)
        assertEquals(someTrack.title, actualTrack.title)
        assertEquals(someTrack.album, actualTrack.album)
        assertEquals(someTrack.genre, actualTrack.genre)
        assertEquals(someTrack.lengthInMillis, actualTrack.trackLength)
    }

    @Test
    fun `throws an exception when adding a track for non-existing artist`() {
        // given
        val track = someTrack()

        // when - then
        val actualThrown =
            assertThrows<DatabaseException> {
                trackRepository.save(track)
            }

        assertEquals(actualThrown.message, "Failure while saving a Track")
    }

    @Test
    fun `fetches all tracks for an artist`() {
        // given
        val artistId = artistRepository.save(someArtist())

        val firstTrack = someTrack(artistId = artistId)
        val secondTrack = someTrack(artistId = artistId)

        val firstTrackId = trackRepository.save(firstTrack)
        val secondTrackId = trackRepository.save(secondTrack)

        // when
        val actualTracks = trackRepository.getTracks(artistId)

        // then
        assertTrue(actualTracks.map { it.id }.containsAll(listOf(firstTrackId, secondTrackId)))
    }
}
