package com.example.spotifyclone.di

import androidx.room.Room
import com.example.spotifyclone.framework.datasource.cache.abstraction.TrackDaoService
import com.example.spotifyclone.framework.datasource.cache.database.Database
import com.example.spotifyclone.framework.datasource.cache.database.TrackDao
import com.example.spotifyclone.framework.datasource.cache.implementation.TrackDaoServiceImpl
import com.example.spotifyclone.framework.datasource.cache.mappers.TrackCacheMapper
import com.example.spotifyclone.framework.presentation.ui.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideNoteDb(
        app: BaseApplication
    ): Database {
        return Room
            .databaseBuilder(app, Database::class.java, Database.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideTrackDao(
        database: Database
    ): TrackDao {
        return database.trackDao()
    }

    @Singleton
    @Provides
    fun provideCacheMapper(): TrackCacheMapper {
        return TrackCacheMapper()
    }

    @Singleton
    @Provides
    fun provideTrackDaoService(
        trackDao: TrackDao,
        cacheMapper: TrackCacheMapper
    ): TrackDaoService {
        return TrackDaoServiceImpl(
            trackDao,
            cacheMapper
        )
    }
}