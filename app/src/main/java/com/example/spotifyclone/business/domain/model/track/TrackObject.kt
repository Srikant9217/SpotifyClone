package com.example.spotifyclone.business.domain.model.track

import com.example.spotifyclone.business.domain.model.artist.ArtistObject
import com.example.spotifyclone.business.domain.model.album.SimplifiedAlbumObject

data class TrackObject(
    val album: SimplifiedAlbumObject,
    val artists: List<ArtistObject>,
    val duration_ms: Int,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)
