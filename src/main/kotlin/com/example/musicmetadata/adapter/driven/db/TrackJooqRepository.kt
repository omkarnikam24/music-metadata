package com.example.musicmetadata.adapter.driven.db

import com.example.musicmetadata.adapter.driven.db.exception.DatabaseException
import com.example.musicmetadata.model.Track
import com.example.musicmetadata.port.driven.TrackRepository
import org.generated.jooq.tables.records.TrackRecord
import org.generated.jooq.tables.references.TRACK
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.UUID

@Repository
class TrackJooqRepository(
    private val jooq: DSLContext,
) : TrackRepository {
    override fun save(track: Track): UUID =
        runCatching {
            jooq.insertInto(TRACK)
                .set(track.toTrackRecord())
                .returningResult(TRACK.ID)
                .fetchInto(UUID::class.java)
                .first()
        }.getOrElse {
            log.error("Failure while saving a Track '${track.title}', error: ${it.message}")
            throw DatabaseException("Failure while saving a Track", it)
        }

    override fun getTracks(artistId: UUID): List<Track> =
        jooq.selectFrom(TRACK)
            .where(TRACK.ARTIST_ID.eq(artistId))
            .fetchInto(TrackRecord::class.java)
            .map { it.toTrack() }

    private fun Track.toTrackRecord() =
        TrackRecord(
            id = id,
            artistId = artistId,
            title = title,
            album = album,
            genre = genre,
            trackLength = lengthInMillis,
            created = OffsetDateTime.now(),
        )

    private fun TrackRecord.toTrack() =
        Track(
            id = id!!,
            artistId = artistId!!,
            title = title!!,
            album = album!!,
            genre = genre!!,
            lengthInMillis = trackLength!!,
        )

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
