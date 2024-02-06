package com.example.musicmetadata.data

import com.example.musicmetadata.adapter.driver.rest.artist.ArtistDto
import com.example.musicmetadata.model.Artist
import com.example.musicmetadata.model.Track
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils
import java.time.LocalDate
import java.util.UUID

fun someTrack(
    id: UUID = UUID.randomUUID(),
    artistId: UUID = UUID.randomUUID(),
) = Track(
    id = id,
    artistId = artistId,
    title = "Some track",
    album = "Interstellar",
    genre = "Rock",
    lengthInMillis = RandomUtils.nextLong(100, 2000),
)

fun someArtist(
    name: String = "Hans Zimmer",
    lastFeaturedOn: LocalDate = LocalDate.now(),
) = Artist(
    name = name,
    lastFeaturedOn = lastFeaturedOn,
)

fun someArtistDto() = ArtistDto(name = "Hans Zimmer")
