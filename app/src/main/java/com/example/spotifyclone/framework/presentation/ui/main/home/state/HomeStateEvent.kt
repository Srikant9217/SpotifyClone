package com.example.spotifyclone.framework.presentation.ui.main.home.state

import com.example.spotifyclone.business.domain.state.StateEvent

sealed class HomeStateEvent : StateEvent {

    object SearchTrackEvent : HomeStateEvent() {
        override fun errorInfo(): String {
            return "Failed to retrieve Track"
        }

        override fun eventName(): String {
            return "SearchTrackEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return true
        }
    }
}