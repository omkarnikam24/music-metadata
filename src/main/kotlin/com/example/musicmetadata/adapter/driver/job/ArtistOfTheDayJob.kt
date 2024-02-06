package com.example.musicmetadata.adapter.driver.job

import com.example.musicmetadata.port.driven.ArtistRepository
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ArtistOfTheDayJob(
    private val artistRepository: ArtistRepository
) {

    @Scheduled(cron = "0 0 0 * * *")
    @SchedulerLock(
        name = "artistOfTheDayLock",
        lockAtLeastFor = "10s",
        lockAtMostFor = "5m"
    )
    @CacheEvict(value = ["artistOfTheDay"], allEntries = true)
    fun updateArtistOfTheDay() {
        log.info("Updating Artist of the day for date: ${LocalDate.now()}")
        artistRepository.updateArtistOfTheDay()
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}