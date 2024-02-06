package com.example.musicmetadata.port.driven

import com.example.musicmetadata.model.Track
import java.util.UUID

interface TrackRepository {
    fun save(track: Track): UUID

    fun getTracks(artistId: UUID): List<Track>
}
