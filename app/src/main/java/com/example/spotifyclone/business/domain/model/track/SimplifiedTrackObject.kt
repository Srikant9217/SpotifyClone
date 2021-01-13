package com.example.spotifyclone.business.domain.model.track

import com.example.spotifyclone.business.domain.model.artist.ArtistObject

data class SimplifiedTrackObject(
    val artists: ArtistObject,
    val name: String,
    val type: String,
    val uri: String
)
