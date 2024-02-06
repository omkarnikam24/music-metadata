package com.example.musicmetadata.adapter.driven.db

import com.example.musicmetadata.IntegrationTest
import com.example.musicmetadata.adapter.driven.db.exception.DatabaseException
import com.example.musicmetadata.data.someArtist
import com.example.musicmetadata.port.driven.AliasRepository
import com.example.musicmetadata.port.driven.ArtistRepository
import org.generated.jooq.tables.references.ARTIST
import org.generated.jooq.tables.references.ARTIST_ALIAS
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

@IntegrationTest
class AliasJooqRepositoryIntegrationTest(
    private val jooq: DSLContext,
    private val artistRepository: ArtistRepository,
    private val aliasRepository: AliasRepository,
) {
    @BeforeEach
    fun init() {
        jooq.truncate(ARTIST).cascade().execute()
    }

    @Test
    fun `saves an alias for a given artist`() {
        // given
        val artist = someArtist()
        val artistId = artistRepository.save(artist)
        val alias = "test Alias"

        // when
        val aliasId = aliasRepository.save(artistId, alias)

        // then
        val actualAlias = jooq.selectFrom(ARTIST_ALIAS).where(ARTIST_ALIAS.ID.eq(aliasId)).fetchOne()

        assertEquals(alias, actualAlias!!.alias)
    }

    @Test
    fun `throws an exception when adding an alias for non-existing artist`() {
        // given
        val artistId = UUID.randomUUID()
        val alias = "test Alias"

        // when - then
        val actualThrown =
            assertThrows<DatabaseException> {
                aliasRepository.save(artistId, alias)
            }

        assertEquals(actualThrown.message, "Failure while saving an Alias")
    }

    @Test
    fun `throws an exception when adding an alias already exists for an artist`() {
        // given
        val artist = someArtist()
        val alias = "test Alias"
        val artistId = artistRepository.save(artist)

        aliasRepository.save(artistId, alias)

        // when - then
        val actualThrown =
            assertThrows<DatabaseException> {
                aliasRepository.save(artistId, alias)
            }

        assertEquals(actualThrown.message, "Failure while saving an Alias")
    }
}
