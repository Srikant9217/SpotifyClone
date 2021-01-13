package com.example.spotifyclone.business.domain.model.artist

import com.example.spotifyclone.business.domain.model.util.FollowersObject
import com.example.spotifyclone.business.domain.model.util.ImageObject

data class ArtistObject(
    val followers: FollowersObject,
    val id: String,
    val images: List<ImageObject>,
    val name: String,
    val type: String,
    val uri: String
)
