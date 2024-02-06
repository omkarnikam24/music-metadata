package com.example.musicmetadata.adapter.driver.rest.artist

import com.example.musicmetadata.model.Artist

data class ArtistDto(
    val name: String,
)

fun Artist.toArtistDto() = ArtistDto(
    name = name
)
