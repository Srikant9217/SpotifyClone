package com.example.spotifyclone.business.domain.model.playlist

import com.example.spotifyclone.business.domain.model.playlist.PlaylistTrackObject
import com.example.spotifyclone.business.domain.model.util.ImageObject
import com.example.spotifyclone.business.domain.model.user.PublicUserObject

data class PlaylistObject(
    val collaborative: Boolean,
    val images: ImageObject,
    val name: String,
    val owner: PublicUserObject,
    val public: Boolean,
    val tracks: PlaylistTrackObject,
    val type: String,
    val uri: String
)
