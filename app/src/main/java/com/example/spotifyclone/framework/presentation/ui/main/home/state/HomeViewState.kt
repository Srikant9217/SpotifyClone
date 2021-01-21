package com.example.spotifyclone.framework.presentation.ui.main.home.state

import com.example.spotifyclone.business.domain.model.track.TrackObject

data class HomeViewState(
    var track: TrackObject? = null
)