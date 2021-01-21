package com.example.spotifyclone.framework.presentation.ui.main.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.hilt.lifecycle.ViewModelInject
import com.example.spotifyclone.business.domain.model.track.TrackObject
import com.example.spotifyclone.business.domain.state.DataState
import com.example.spotifyclone.business.domain.state.StateEvent
import com.example.spotifyclone.business.interactors.home.SearchTracks
import com.example.spotifyclone.framework.presentation.ui.BaseViewModel
import com.example.spotifyclone.framework.presentation.ui.main.home.state.HomeStateEvent.*
import com.example.spotifyclone.framework.presentation.ui.main.home.state.HomeViewState
import kotlinx.coroutines.flow.Flow

class HomeViewModel
@ViewModelInject
constructor(
    private val searchTracks: SearchTracks
) : BaseViewModel<HomeViewState>() {

    override fun initNewViewState(): HomeViewState {
        return HomeViewState()
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<HomeViewState>?> = when (stateEvent) {
            is SearchTrackEvent -> {
                searchTracks.searchTrackById(
                    id = "4IWZsfEkaK49itBwCTFDXQ",
                    stateEvent = stateEvent
                )
            }
            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }
        launchJob(stateEvent, job)
    }

    override fun handleNewData(data: HomeViewState) {
        data.track?.let {
            setTrack(it)
        }
    }

    val track: MutableState<TrackObject?> = mutableStateOf(getCurrentViewStateOrNew().track)

    val displayProgressBar = mutableStateOf(false)

    private fun setTrack(currentTrack: TrackObject) {
        val update = getCurrentViewStateOrNew()
        update.track = currentTrack
        track.value = currentTrack
        setViewState(update)
    }
}