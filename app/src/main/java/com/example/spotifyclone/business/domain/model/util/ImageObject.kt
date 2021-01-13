package com.example.spotifyclone.business.domain.model.util

data class ImageObject(
    val image: List<Image>
){
    data class Image(
        val width: Int,
        val height: Int,
        val url: String
    )
}