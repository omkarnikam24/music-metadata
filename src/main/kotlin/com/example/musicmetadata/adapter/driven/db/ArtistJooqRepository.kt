package com.example.musicmetadata.adapter.driven.db

import com.example.musicmetadata.adapter.driven.db.exception.DatabaseException
import com.example.musicmetadata.model.Artist
import com.example.musicmetadata.port.driven.ArtistRepository
import com.example.musicmetadata.port.driven.ArtistUpdateCommand
import org.generated.jooq.tables.records.ArtistRecord
import org.generated.jooq.tables.references.ARTIST
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

@Repository
class ArtistJooqRepository(
    private val jooq: DSLContext,
) : ArtistRepository {
    override fun save(artist: Artist): UUID =
        runCatching {
            jooq.insertInto(ARTIST)
                .set(artist.toArtistRecord())
                .returningResult(ARTIST.ID)
                .fetchInto(UUID::class.java)
                .first()
        }.getOrElse {
            log.error("Failure while saving an Artist '${artist.name}', error: ${it.message}")
            throw DatabaseException("Failure while saving an Artist", it)
        }

    override fun update(
        artistId: UUID,
        artistUpdateCommand: ArtistUpdateCommand,
    ) {
        jooq.update(ARTIST)
            .set(artistUpdateCommand.toFieldMap())
            .where(ARTIST.ID.eq(artistId))
            .execute().also {
                log.info("Updated Artist: $artistId, affectedRows: $it")
            }
    }

    override fun getArtistOfTheDay(): Artist {
        val today = LocalDate.now()
        val artistRecord = jooq.selectFrom(ARTIST)
            .where(ARTIST.LAST_FEATURED_ON.eq(today))
            .fetchOne()
            .takeIf { it != null } ?: throw DatabaseException("No Artist of the day found for date: $today")

        return artistRecord.toArtist()
    }

    override fun updateArtistOfTheDay() {
        val today = LocalDate.now()
        jooq.update(ARTIST)
            .set(ARTIST.LAST_FEATURED_ON, today)
            .orderBy(ARTIST.LAST_FEATURED_ON.nullsFirst())
            .limit(1)
            .execute()
            .takeIf { it <= 1 }
            ?: {
                log.error("More than 1 artists unexpectedly updated as Artists of the day for date: $today")
                throw DatabaseException("More than 1 artists unexpectedly updated as Artists of the day for date: $today")
            }
    }

    private fun Artist.toArtistRecord() =
        ArtistRecord(
            id = UUID.randomUUID(),
            name = name,
            created = OffsetDateTime.now(),
        )

    private fun ArtistRecord.toArtist() =
        Artist(
            name = name!!,
            lastFeaturedOn = lastFeaturedOn!!,
        )

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
