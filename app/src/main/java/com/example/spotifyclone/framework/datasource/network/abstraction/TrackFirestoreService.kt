package com.example.spotifyclone.framework.datasource.network.abstraction

import com.example.spotifyclone.business.domain.model.track.TrackObject

interface TrackFirestoreService{

    suspend fun insertTrack(track: TrackObject)

    suspend fun searchTrack(id: String): TrackObject?

    suspend fun getAllTracks(): List<TrackObject>

    suspend fun deleteTrack(id: String)

    suspend fun deleteAllTracks()
}
