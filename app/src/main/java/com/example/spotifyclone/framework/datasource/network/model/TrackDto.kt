package com.example.spotifyclone.framework.datasource.network.model

import com.example.spotifyclone.business.domain.model.album.SimplifiedAlbumObject
import com.example.spotifyclone.business.domain.model.artist.ArtistObject

data class TrackDto(
    val album: SimplifiedAlbumObject,
    val artists: List<ArtistObject>,
    val duration_ms: Int,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)