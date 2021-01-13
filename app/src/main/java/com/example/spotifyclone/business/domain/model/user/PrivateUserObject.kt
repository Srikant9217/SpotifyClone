package com.example.spotifyclone.business.domain.model.user

import com.example.spotifyclone.business.domain.model.util.FollowersObject
import com.example.spotifyclone.business.domain.model.util.ImageObject

data class PrivateUserObject(
    val display_name:String,
    val followers: FollowersObject,
    val images: ImageObject,
    val type: String,
    val uri: String
)
