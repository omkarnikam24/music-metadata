package com.example.musicmetadata.adapter.driver.rest.track

import com.example.musicmetadata.port.driver.TrackMetadataRequestHandler
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*

@RestController
@RequestMapping("/tracks")
class TrackMetadataController(
    private val trackMetadataService: TrackMetadataRequestHandler,
) {
    @Operation(summary = "Create a track for an artist")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Track created"),
        ApiResponse(responseCode = "400", description = "Invalid artist Id"),
        ApiResponse(responseCode = "500", description = "Unsupported result")
    )
    @PostMapping("/create")
    fun createTrack(
        @RequestBody track: TrackDto,
    ): ResponseEntity<Any> {
        with(trackMetadataService.save(track.toTrack())) {
            return ResponseEntity.created(createResourceLocation(this)).build()
        }
    }

    @Operation(summary = "Get all tracks for a specified artist")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Found tracks"),
        ApiResponse(responseCode = "500", description = "Unsupported result")
    )
    @GetMapping(params = [""])
    fun getTracks(
        @RequestParam artistId: UUID,
    ): ResponseEntity<List<TrackDto>> {
        with(trackMetadataService.getTracks(artistId)) {
            return ResponseEntity.ok(
                this.map {
                    it.toTrackDto()
                },
            )
        }
    }

    private fun createResourceLocation(id: UUID) =
        ServletUriComponentsBuilder
            .newInstance()
            .path("/tracks/{id}")
            .buildAndExpand(id)
            .toUri()
}
