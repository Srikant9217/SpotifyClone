package com.example.spotifyclone.framework.datasource.cache.implementation

import com.example.spotifyclone.business.domain.model.track.TrackObject
import com.example.spotifyclone.framework.datasource.cache.abstraction.TrackDaoService
import com.example.spotifyclone.framework.datasource.cache.database.TrackDao
import com.example.spotifyclone.framework.datasource.cache.mappers.TrackCacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackDaoServiceImpl
@Inject
constructor(
    private val trackDao: TrackDao,
    private val trackMapper: TrackCacheMapper
) : TrackDaoService {

    override suspend fun insertTrack(track: TrackObject): Long {
        return trackDao.insertTrack(trackMapper.mapFromDomainModel(track))
    }

    override suspend fun insertTracks(tracks: List<TrackObject>): LongArray {
        return trackDao.insertTracks(trackMapper.mapListFromDomainModel(tracks))
    }

    override suspend fun searchTrackById(id: String): TrackObject? {
        return trackDao.searchTrackById(id)?.let {
            trackMapper.mapToDomainModel(it)
        }
    }

    override suspend fun getNumTracks(): Int {
        return trackDao.getNumTracks()
    }

    override suspend fun getAllTracks(): List<TrackObject> {
        return trackMapper.mapListToDomainModel(trackDao.getAllTracks())
    }

    override suspend fun deleteTrack(id: String): Int {
        return trackDao.deleteTrack(id)
    }

    override suspend fun deleteTracks(tracks: List<TrackObject>): Int {
        return trackDao.deleteTracks(trackMapper.mapListFromDomainModel(tracks))
    }
}