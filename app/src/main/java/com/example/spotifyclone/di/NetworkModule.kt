package com.example.spotifyclone.di

import com.example.spotifyclone.business.data.network.abstraction.TrackNetworkDataSource
import com.example.spotifyclone.business.data.network.implementation.TrackNetworkDataSourceImpl
import com.example.spotifyclone.framework.datasource.network.abstraction.TrackFirestoreService
import com.example.spotifyclone.framework.datasource.network.abstraction.TrackService
import com.example.spotifyclone.framework.datasource.network.implementation.TrackFirestoreServiceImpl
import com.example.spotifyclone.framework.datasource.network.interceptor.AuthInterceptor
import com.example.spotifyclone.framework.datasource.network.mappers.TrackNetworkMapper
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkMapper(): TrackNetworkMapper {
        return TrackNetworkMapper()
    }

    @Singleton
    @Provides
    fun provideFirestoreService(
        networkMapper: TrackNetworkMapper
    ): TrackFirestoreService {
        return TrackFirestoreServiceImpl(
            networkMapper
        )
    }

    @Singleton
    @Provides
    fun provideTrackNetworkDataSource(
        trackFirestoreService: TrackFirestoreService
    ): TrackNetworkDataSource {
        return TrackNetworkDataSourceImpl(
            trackFirestoreService
        )
    }

    @Singleton
    @Provides
    fun provideOkhttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideTrackService(
        okHttpClient: OkHttpClient
    ): TrackService {
        return Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(TrackService::class.java)
    }
}