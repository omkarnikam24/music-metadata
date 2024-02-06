package com.example.musicmetadata.port.driven

import java.util.UUID

interface AliasRepository {
    fun save(
        artistId: UUID,
        alias: String,
    ): UUID
}
