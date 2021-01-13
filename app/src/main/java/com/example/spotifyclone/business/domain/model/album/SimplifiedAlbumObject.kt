package com.example.spotifyclone.business.domain.model.album

import com.example.spotifyclone.business.domain.model.util.ImageObject
import com.example.spotifyclone.business.domain.model.artist.SimplifiedArtistObject

data class SimplifiedAlbumObject(
    val artists: List<SimplifiedArtistObject>,
    val id: String,
    val images: List<ImageObject>,
    val name: String,
    val type: String,
    val uri: String
)
