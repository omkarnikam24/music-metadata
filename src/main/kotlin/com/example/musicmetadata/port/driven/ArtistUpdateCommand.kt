package com.example.musicmetadata.port.driven

import org.generated.jooq.tables.references.ARTIST

interface ArtistUpdateCommand {
    fun toFieldMap(): Map<String, Any>
}

data class ArtistNameCommand(
    val value: Any,
) : ArtistUpdateCommand {
    override fun toFieldMap() = mapOf(ARTIST.NAME.name to value)
}

data class ArtistLastFeaturedOnCommand(
    val value: Any,
) : ArtistUpdateCommand {
    override fun toFieldMap() = mapOf(ARTIST.LAST_FEATURED_ON.name to value)
}
