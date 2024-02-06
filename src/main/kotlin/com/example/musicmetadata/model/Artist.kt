package com.example.musicmetadata.model

import java.time.LocalDate

data class Artist(
    val name: String,
    val lastFeaturedOn: LocalDate?,
)
