package com.example.spotifyclone.framework.datasource.network.implementation

import com.example.spotifyclone.business.domain.model.track.TrackObject
import com.example.spotifyclone.framework.datasource.network.abstraction.TrackFirestoreService
import com.example.spotifyclone.framework.datasource.network.mappers.TrackNetworkMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackFirestoreServiceImpl
@Inject
constructor(
    private val networkMapper: TrackNetworkMapper
): TrackFirestoreService {

    override suspend fun insertTrack(track: TrackObject) {
        TODO("Not yet implemented")
    }

    override suspend fun searchTrack(id: String): TrackObject? {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTracks(): List<TrackObject> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTrack(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTracks() {
        TODO("Not yet implemented")
    }
}