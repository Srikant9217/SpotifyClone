package com.example.spotifyclone.framework.datasource.network.abstraction

import com.example.spotifyclone.business.domain.model.track.TrackObject
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TrackService {

    @GET("v1/tracks/{id}")
    suspend fun searchTrack(
        @Path("id") id: String
    ): TrackObject
}
