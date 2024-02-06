package com.example.musicmetadata.port.driver

import com.example.musicmetadata.model.Track
import java.util.UUID

interface TrackMetadataRequestHandler {
    fun save(track: Track): UUID

    fun getTracks(artistId: UUID): List<Track>
}
