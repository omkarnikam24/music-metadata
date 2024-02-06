package com.example.musicmetadata.initializer

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class DatabaseInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    companion object {
        private val container: PostgreSQLContainer<*> =
            PostgreSQLContainer(
                DockerImageName.parse("postgres"),
            ).apply { start() }
    }

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        TestPropertyValues.of(
            "spring.datasource.url=${container.jdbcUrl}",
            "spring.datasource.username=${container.username}",
            "spring.datasource.password=${container.password}",
        ).applyTo(applicationContext)
        System.setProperty("db.url", container.jdbcUrl)
        System.setProperty("db.username", container.username)
        System.setProperty("db.password", container.password)
    }
}
