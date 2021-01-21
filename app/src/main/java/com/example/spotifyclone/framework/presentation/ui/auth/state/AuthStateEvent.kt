package com.example.spotifyclone.framework.presentation.ui.auth.state

import com.example.spotifyclone.business.domain.state.StateEvent

sealed class AuthStateEvent : StateEvent {

    object CheckSpotifyPackage : AuthStateEvent() {
        override fun errorInfo(): String {
            return "Failed to check if Spotify is installed on the device"
        }

        override fun eventName(): String {
            return "CheckSpotifyPackage"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return false
        }
    }

    object StartFirebaseAuthentication : AuthStateEvent() {
        override fun errorInfo(): String {
            return "Authentication failed"
        }

        override fun eventName(): String {
            return "StartFirebaseAuthentication"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return false
        }
    }

    object GetSpotifyToken : AuthStateEvent() {
        override fun errorInfo(): String {
            return "Failed to get Spotify token"
        }

        override fun eventName(): String {
            return "GetSpotifyToken"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return false
        }
    }
}