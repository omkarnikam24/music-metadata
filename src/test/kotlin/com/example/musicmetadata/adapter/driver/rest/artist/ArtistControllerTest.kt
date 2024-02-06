package com.example.musicmetadata.adapter.driver.rest.artist

import com.example.musicmetadata.data.someArtist
import com.example.musicmetadata.data.someArtistDto
import com.example.musicmetadata.port.driver.ArtistRequestHandler
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils
import java.util.*

@WebMvcTest(ArtistController::class)
class ArtistControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockkBean
    private lateinit var artistRequestHandler: ArtistRequestHandler

    @Test
    fun `returns successful when artist name is edited`() {
        // given
        val artistId = UUID.randomUUID()
        val artistDto = someArtistDto()

        every { artistRequestHandler.updateArtistName(artistId, artistDto.name) } just Runs

        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/artist/$artistId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(artistDto))
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)

    }

    @Test
    fun `returns artist of the day`() {
        // given
        val artist = someArtist()

        every { artistRequestHandler.getArtistOfTheDay() } returns artist

        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/artist/artist-of-the-day")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(artist.toArtistDto())))
    }

    @Test
    fun `returns 400 when artistId is invalid`() {
        // given
        val artistId = RandomStringUtils.randomAlphanumeric(20)
        val artistDto = someArtistDto()

        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/artist/$artistId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(artistDto))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}