package com.example.spotifyclone.business.data.cache.abstraction

import com.example.spotifyclone.business.domain.model.track.TrackObject

interface TrackCacheDataSource{

    suspend fun insertTrack(track: TrackObject): Long

    suspend fun insertTracks(tracks: List<TrackObject>): LongArray

    suspend fun searchTrackById(id: String): TrackObject?

    suspend fun getNumTracks(): Int

    suspend fun getAllTracks(): List<TrackObject>

    suspend fun deleteTrack(primaryKey: String): Int

    suspend fun deleteTracks(tracks: List<TrackObject>): Int
}