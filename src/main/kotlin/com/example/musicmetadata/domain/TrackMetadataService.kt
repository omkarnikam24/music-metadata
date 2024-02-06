package com.example.musicmetadata.domain

import com.example.musicmetadata.model.Track
import com.example.musicmetadata.port.driven.TrackRepository
import com.example.musicmetadata.port.driver.TrackMetadataRequestHandler
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TrackMetadataService(
    private val trackRepository: TrackRepository,
) : TrackMetadataRequestHandler {
    override fun save(track: Track) = trackRepository.save(track)

    override fun getTracks(artistId: UUID) = trackRepository.getTracks(artistId)
}
