package com.example.musicmetadata.model

import java.util.UUID

data class Track(
    val id: UUID,
    val artistId: UUID,
    val title: String,
    val album: String,
    val genre: String,
    val lengthInMillis: Long,
)
