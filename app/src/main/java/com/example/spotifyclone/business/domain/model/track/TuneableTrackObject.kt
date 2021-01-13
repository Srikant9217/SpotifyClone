package com.example.spotifyclone.business.domain.model.track

data class TuneableTrackObject(
    val acousticness: Float,
    val danceability: Float,
    val duration_ms: Int,
    val energy: Float,
    val instrumentalness: Float,
    val key: Int,
    val liveness: Float,
    val loudness: Float,
    val mode: Int,
    val popularity: Float,
    val speechiness: Float,
    val tempo: Float,
    val time_signature: Int,
    val valence: Float
)
