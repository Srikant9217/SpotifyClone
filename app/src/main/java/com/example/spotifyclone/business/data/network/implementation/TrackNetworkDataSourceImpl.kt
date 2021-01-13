package com.example.spotifyclone.business.data.network.implementation

import com.example.spotifyclone.business.data.network.abstraction.TrackNetworkDataSource
import com.example.spotifyclone.business.domain.model.track.TrackObject
import com.example.spotifyclone.framework.datasource.network.abstraction.TrackFirestoreService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackNetworkDataSourceImpl
@Inject
constructor(
    private val firestoreService: TrackFirestoreService
): TrackNetworkDataSource {

    override suspend fun insertTrack(track: TrackObject) {
        return firestoreService.insertTrack(track)
    }

    override suspend fun searchTrack(id: String): TrackObject? {
        return firestoreService.searchTrack(id)
    }

    override suspend fun getAllTracks(): List<TrackObject> {
        return firestoreService.getAllTracks()
    }

    override suspend fun deleteTrack(id: String) {
        return firestoreService.deleteTrack(id)
    }

    override suspend fun deleteAllTracks() {
        return firestoreService.deleteAllTracks()
    }
}