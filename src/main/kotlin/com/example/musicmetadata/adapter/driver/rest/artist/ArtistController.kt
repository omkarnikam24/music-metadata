package com.example.musicmetadata.adapter.driver.rest.artist

import com.example.musicmetadata.port.driver.ArtistRequestHandler
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/artist")
class ArtistController(
    private val artistRequestHandler: ArtistRequestHandler,
) {

    @Operation(summary = "Update artist name and add it to artist's aliases")
    @ApiResponses(
        ApiResponse(responseCode = "204", description = "Successfully updated"),
        ApiResponse(responseCode = "400", description = "Invalid artist Id"),
        ApiResponse(responseCode = "500", description = "Unsupported result")
    )
    @PatchMapping("/{artistId}")
    fun editArtistName(
        @PathVariable artistId: UUID,
        @RequestBody artistDto: ArtistDto,
    ): ResponseEntity<Any> {
        artistRequestHandler.updateArtistName(artistId, artistDto.name)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Get Artist of the day")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Found Artist of the day"),
        ApiResponse(responseCode = "500", description = "Unsupported result")
    )
    @GetMapping("/artist-of-the-day")
    fun getArtistOfTheDay(): ResponseEntity<ArtistDto> {
        with(artistRequestHandler.getArtistOfTheDay()) {
            return ResponseEntity.ok(
                this.toArtistDto()
            )
        }
    }
}
