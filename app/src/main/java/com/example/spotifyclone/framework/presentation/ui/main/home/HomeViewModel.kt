package com.example.spotifyclone.framework.presentation.ui.main.home

import com.example.spotifyclone.business.domain.model.track.TrackObject
import com.example.spotifyclone.business.domain.state.DataState
import com.example.spotifyclone.business.domain.state.StateEvent
import com.example.spotifyclone.business.interactors.home.SearchTracks
import com.example.spotifyclone.framework.presentation.ui.BaseViewModel
import com.example.spotifyclone.framework.presentation.ui.main.home.state.HomeStateEvent.SearchTrackEvent
import com.example.spotifyclone.framework.presentation.ui.main.home.state.HomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
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

    private fun setTrack(currentTrack: TrackObject) {
        viewState.value.track = currentTrack
    }
}