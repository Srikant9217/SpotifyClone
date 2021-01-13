package com.example.spotifyclone.business.domain.model.album

import com.example.spotifyclone.business.domain.model.artist.ArtistObject
import com.example.spotifyclone.business.domain.model.util.ImageObject
import com.example.spotifyclone.business.domain.model.track.SimplifiedTrackObject

data class AlbumObject(
    val artists: List<ArtistObject>,
    val id: String,
    val images: List<ImageObject>,
    val name: String,
    val release_date:String,
    val tracks: List<SimplifiedTrackObject>,
    val type: String,
    val uri: String
)
