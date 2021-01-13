package com.example.spotifyclone.framework.datasource.cache.database

import androidx.room.*
import com.example.spotifyclone.business.domain.model.track.TrackObject
import com.example.spotifyclone.framework.datasource.cache.model.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTracks(tracks: List<TrackEntity>): LongArray

    @Query("SELECT * FROM track_table WHERE id = :id")
    suspend fun searchTrackById(id: String): TrackEntity?

    @Query("SELECT COUNT(*) FROM track_table")
    suspend fun getNumTracks(): Int

    @Query("SELECT * FROM track_table")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("DELETE FROM track_table WHERE id = :id")
    suspend fun deleteTrack(id: String): Int

    @Delete
    suspend fun deleteTracks(tracks: List<TrackEntity>): Int
}