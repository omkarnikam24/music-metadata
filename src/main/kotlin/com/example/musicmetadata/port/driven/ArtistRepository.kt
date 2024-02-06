package com.example.musicmetadata.port.driven

import com.example.musicmetadata.model.Artist
import java.util.*

interface ArtistRepository {
    fun save(artist: Artist): UUID

    fun update(
        artistId: UUID,
        artistUpdateCommand: ArtistUpdateCommand,
    )

    fun getArtistOfTheDay(): Artist

    fun updateArtistOfTheDay()
}
