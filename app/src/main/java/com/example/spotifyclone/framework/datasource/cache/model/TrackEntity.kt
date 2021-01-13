package com.example.spotifyclone.framework.datasource.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.spotifyclone.business.domain.model.album.SimplifiedAlbumObject
import com.example.spotifyclone.business.domain.model.artist.ArtistObject

@Entity(tableName = "track_table")
data class TrackEntity(
    val album: SimplifiedAlbumObject,
    val artists: List<ArtistObject>,
    val duration_ms: Int,
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)