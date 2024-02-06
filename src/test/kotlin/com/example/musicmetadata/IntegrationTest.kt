package com.example.musicmetadata

import com.example.musicmetadata.initializer.DatabaseInitializer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.TestConstructor.AutowireMode.ALL

@SpringBootTest(
    classes = [MusicMetadataApplication::class],
)
@ContextConfiguration(initializers = [DatabaseInitializer::class])
@TestConstructor(autowireMode = ALL)
@Target(AnnotationTarget.CLASS)
annotation class IntegrationTest
