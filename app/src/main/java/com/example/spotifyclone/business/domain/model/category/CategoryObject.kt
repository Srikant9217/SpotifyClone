package com.example.spotifyclone.business.domain.model.category

import com.example.spotifyclone.business.domain.model.util.ImageObject

data class CategoryObject(
    val icons: List<ImageObject>,
    val id : String,
    val name: String
)
