package com.example.spotifyclone.business.data.cache.implementation

import com.example.spotifyclone.business.data.cache.abstraction.TrackCacheDataSource
import com.example.spotifyclone.business.domain.model.track.TrackObject
import com.example.spotifyclone.framework.datasource.cache.abstraction.TrackDaoService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackCacheDataSourceImpl
@Inject
constructor(
    private val trackDaoService: TrackDaoService
): TrackCacheDataSource {

    override suspend fun insertTrack(track: TrackObject): Long {
        return trackDaoService.insertTrack(track)
    }

    override suspend fun insertTracks(tracks: List<TrackObject>): LongArray {
        return trackDaoService.insertTracks(tracks)
    }

    override suspend fun searchTrackById(id: String): TrackObject? {
        return trackDaoService.searchTrackById(id)
    }

    override suspend fun getNumTracks(): Int {
        return trackDaoService.getNumTracks()
    }

    override suspend fun getAllTracks(): List<TrackObject> {
        return trackDaoService.getAllTracks()
    }

    override suspend fun deleteTrack(primaryKey: String): Int {
        return trackDaoService.deleteTrack(primaryKey)
    }

    override suspend fun deleteTracks(tracks: List<TrackObject>): Int {
        return trackDaoService.deleteTracks(tracks)
    }
}