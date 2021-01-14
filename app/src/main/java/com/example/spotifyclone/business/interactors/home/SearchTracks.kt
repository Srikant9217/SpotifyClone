package com.example.spotifyclone.business.interactors.home

import com.example.spotifyclone.business.data.network.ApiResponseHandler
import com.example.spotifyclone.business.data.network.abstraction.TrackNetworkDataSource
import com.example.spotifyclone.business.data.util.safeApiCall
import com.example.spotifyclone.business.domain.model.track.TrackObject
import com.example.spotifyclone.business.domain.state.*
import com.example.spotifyclone.framework.datasource.network.abstraction.TrackService
import com.example.spotifyclone.framework.datasource.preferances.MyPreferences
import com.example.spotifyclone.framework.presentation.ui.home.state.HomeViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchTracks
@Inject
constructor(
    private val trackService: TrackService
) {
    fun searchTrackById(
        id: String,
        stateEvent: StateEvent
    ): Flow<DataState<HomeViewState>?> = flow {

        val networkResult = safeApiCall(Dispatchers.IO) {
            trackService.searchTrack(id)
        }

        val response = object : ApiResponseHandler<HomeViewState, TrackObject>(
            response = networkResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: TrackObject): DataState<HomeViewState> {
                return DataState.data(
                    response = Response(
                        message = null,
                        uiComponentType = UIComponentType.None,
                        messageType = MessageType.Success
                    ),
                    data = HomeViewState(
                        track = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }
}