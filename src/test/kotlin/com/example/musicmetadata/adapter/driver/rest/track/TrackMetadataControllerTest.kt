package com.example.musicmetadata.adapter.driver.rest.track

import com.example.musicmetadata.data.someTrack
import com.example.musicmetadata.port.driver.TrackMetadataRequestHandler
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils
import java.util.UUID

@WebMvcTest(TrackMetadataController::class)
class TrackMetadataControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockkBean
    private lateinit var trackMetadataRequestHandler: TrackMetadataRequestHandler

    @Test
    fun `returns successful when a track is added for a given artist`() {
        // given
        val someTrack = someTrack()
        val trackDto = someTrack.toTrackDto()
        every { trackMetadataRequestHandler.save(any()) } returns someTrack.id

        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/tracks/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(trackDto)),
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.header().string("Location", "/tracks/${someTrack.id}"))
    }

    @Test
    fun `returns tracks for a given artist`() {
        // given
        val artistId = UUID.randomUUID()
        val someTrack = someTrack()
        every { trackMetadataRequestHandler.getTracks(artistId) } returns listOf(someTrack)

        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/tracks").queryParam("artistId", artistId.toString()),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `returns 400 when artistId is invalid`() {
        // given
        val artistId = RandomStringUtils.randomAlphanumeric(20)

        // when - then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/tracks").param("artistId", artistId),
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}
