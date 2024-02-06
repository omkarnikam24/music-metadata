package com.example.musicmetadata.adapter.driver.rest.track

import com.example.musicmetadata.model.Track
import java.util.UUID

data class TrackDto(
    val artistId: UUID,
    val title: String,
    val album: String,
    val genre: String,
    val length: Long,
)

fun TrackDto.toTrack() =
    Track(
        id = UUID.randomUUID(),
        artistId = artistId,
        title = title,
        album = album,
        genre = genre,
        lengthInMillis = length,
    )

fun Track.toTrackDto() =
    TrackDto(
        artistId = artistId,
        title = title,
        album = album,
        genre = genre,
        length = lengthInMillis,
    )
