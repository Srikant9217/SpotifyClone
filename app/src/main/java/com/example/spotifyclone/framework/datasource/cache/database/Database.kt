package com.example.spotifyclone.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.spotifyclone.framework.datasource.cache.model.TrackEntity

@Database(entities = [TrackEntity::class ], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database: RoomDatabase() {

    abstract fun trackDao(): TrackDao


    companion object{
        const val DATABASE_NAME: String = "track_db"
    }
}