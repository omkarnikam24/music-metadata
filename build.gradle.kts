import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.flywaydb.gradle.task.FlywayMigrateTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.testcontainers.containers.PostgreSQLContainer

plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    id("io.github.usefulness.ktlint-gradle-plugin") version "0.8.1"
    id("org.jooq.jooq-codegen-gradle") version "3.19.1"
    id("org.flywaydb.flyway") version "10.7.1"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jooq") {
        exclude(group = "org.jooq", module = "jooq")
    }
    implementation("org.jooq:jooq:3.19.1")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.flywaydb:flyway-core:10.7.1")
    implementation("net.javacrumbs.shedlock:shedlock-spring:5.10.2")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
    implementation("org.flywaydb:flyway-database-postgresql:10.7.1")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("io.mockk:mockk:1.13.9")

    jooqCodegen("org.postgresql:postgresql")
}

buildscript {
    dependencies {
        classpath("org.testcontainers:postgresql:1.17.5")
        classpath("org.flywaydb:flyway-core:10.7.1")
        classpath("org.flywaydb:flyway-database-postgresql:10.7.1")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
    dependsOn("dbMigrate")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register("dbMigrate") {
    dependsOn(tasks.flywayMigrate, tasks.named("jooqCodegen"))
    doLast { container.stop() }
}

val container = PostgreSQLContainer("postgres")

tasks.register("startPostgresContainer") {
    doLast {
        val port = 5432
        container
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("test")
            .withExposedPorts(port)
            .withCreateContainerCmdModifier {
                it.withHostConfig(
                    HostConfig.newHostConfig()
                        .withPortBindings(PortBinding(Ports.Binding.bindPort(port), ExposedPort(port))),
                )
            }
        container.start()
    }
}

tasks.withType<FlywayMigrateTask> {
    dependsOn(tasks.named("startPostgresContainer"))

    doFirst {
        driver = "org.postgresql.Driver"
        url = "jdbc:postgresql://localhost:5432/postgres"
        user = "postgres"
        password = "test"
    }
}

jooq {
    configuration {
        jdbc {
            driver = "org.postgresql.Driver"
            url = "jdbc:postgresql://localhost:5432/postgres"
            user = "postgres"
            password = "test"
        }

        generator {
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                includes = ".*"
                excludes = """
           UNUSED_TABLE                # This table (unqualified name) should not be generated
         | PREFIX_.*                   # Objects with a given prefix should not be generated
         | SECRET_SCHEMA\.SECRET_TABLE # This table (qualified name) should not be generated
         | SECRET_ROUTINE              # This routine (unqualified name) ...
      """
                inputSchema = "public"
            }

            generate {
                name = "org.jooq.codegen.KotlinGenerator"
                withJooqVersionReference(false)
            }
            target {
                packageName = "org.generated.jooq"
                directory = "build/generated-src/jooq"
            }
        }
    }
}
