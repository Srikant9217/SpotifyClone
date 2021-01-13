package com.example.spotifyclone.business.domain.model.util

import java.util.*

data class PagingObject(
    val href: String,
    val items: List<Objects>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String,
    val total: Int,
)
