package com.example.musicmetadata.adapter.driven.db

import com.example.musicmetadata.adapter.driven.db.exception.DatabaseException
import com.example.musicmetadata.port.driven.AliasRepository
import org.generated.jooq.tables.references.ARTIST_ALIAS
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime
import java.util.UUID

@Repository
class AliasJooqRepository(
    private val jooq: DSLContext,
) : AliasRepository {
    override fun save(
        artistId: UUID,
        alias: String,
    ): UUID =
        runCatching {
            jooq.insertInto(
                ARTIST_ALIAS,
                ARTIST_ALIAS.ID,
                ARTIST_ALIAS.ARTIST_ID,
                ARTIST_ALIAS.ALIAS,
                ARTIST_ALIAS.CREATED,
            )
                .values(UUID.randomUUID(), artistId, alias, OffsetDateTime.now())
                .returningResult(ARTIST_ALIAS.ID)
                .fetchInto(UUID::class.java)
                .first()
        }.getOrElse {
            log.error("Failure while saving an Alias '$alias' for Artist: $artistId, error: ${it.message}")
            throw DatabaseException("Failure while saving an Alias", it)
        }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
