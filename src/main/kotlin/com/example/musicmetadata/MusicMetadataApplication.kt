package com.example.musicmetadata

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class MusicMetadataApplication

fun main(args: Array<String>) {
    runApplication<MusicMetadataApplication>(*args)
}
