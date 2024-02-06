package com.example.musicmetadata.domain

import com.example.musicmetadata.model.Artist
import com.example.musicmetadata.port.driven.AliasRepository
import com.example.musicmetadata.port.driven.ArtistLastFeaturedOnCommand
import com.example.musicmetadata.port.driven.ArtistNameCommand
import com.example.musicmetadata.port.driven.ArtistRepository
import com.example.musicmetadata.port.driver.ArtistRequestHandler
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@Service
class ArtistService(
    private val artistRepository: ArtistRepository,
    private val aliasRepository: AliasRepository,
) : ArtistRequestHandler {
    @Transactional
    override fun updateArtistName(
        artistId: UUID,
        artistName: String,
    ) {
        log.info("process=update_artist_name, status=start, artistId=$artistId, artistName=$artistName")

        artistRepository.update(artistId, ArtistNameCommand(artistName))
        aliasRepository.save(artistId, artistName)

        log.info("process=update_artist_name, status=done, artistId=$artistId, artistName=$artistName")
    }

    override fun updateArtistLastFeaturedOn(
        artistId: UUID,
        lastFeaturedOn: LocalDate,
    ) {
        log.info("process=update_artist_last_featured_on, status=start, artistId=$artistId, lastFeaturedOn=$lastFeaturedOn")

        artistRepository.update(artistId, ArtistLastFeaturedOnCommand(lastFeaturedOn))

        log.info("process=update_artist_last_featured_on, status=done, artistId=$artistId, lastFeaturedOn=$lastFeaturedOn")
    }

    @Cacheable("artistOfTheDay")
    override fun getArtistOfTheDay(): Artist {
        log.info("process=get_artist_of_the_day, status=start")
        return artistRepository.getArtistOfTheDay()
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}
