package com.example.musicmetadata.port.driver

import com.example.musicmetadata.model.Artist
import java.time.LocalDate
import java.util.UUID

interface ArtistRequestHandler {
    fun updateArtistName(
        artistId: UUID,
        artistName: String,
    )

    fun updateArtistLastFeaturedOn(
        artistId: UUID,
        lastFeaturedOn: LocalDate,
    )

    fun getArtistOfTheDay(): Artist
}
