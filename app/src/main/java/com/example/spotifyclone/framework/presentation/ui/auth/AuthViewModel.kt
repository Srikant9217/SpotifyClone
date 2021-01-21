package com.example.spotifyclone.framework.presentation.ui.auth

import androidx.compose.runtime.mutableStateOf
import com.example.spotifyclone.business.domain.state.*
import com.example.spotifyclone.framework.presentation.ui.BaseViewModel
import com.example.spotifyclone.framework.presentation.ui.auth.state.AuthStateEvent.*
import com.example.spotifyclone.framework.presentation.ui.auth.state.AuthViewState

class AuthViewModel : BaseViewModel<AuthViewState>() {

    val splashLogo = mutableStateOf(true)

    val spotifyNotInstalledDialog = mutableStateOf(false)

    val firebaseErrorDialog = mutableStateOf(false)
    val firebaseErrorMessage = mutableStateOf("")

    val spotifyTokenDialog = mutableStateOf(false)
    val spotifyTokenErrorMessage = mutableStateOf("")

    fun setFirebaseErrorDialog(bool: Boolean, message: String) {
        firebaseErrorDialog.value = bool
        firebaseErrorMessage.value = message
    }

    fun setSpotifyTokenErrorDialog(bool: Boolean, message: String) {
        spotifyTokenDialog.value = bool
        spotifyTokenErrorMessage.value = message
    }


    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        when (stateEvent) {
            is CheckSpotifyPackage -> {
                checkSpotifyPackageEvent()
            }
            is StartFirebaseAuthentication -> {
                startFirebaseAuthenticationEvent()
            }
            is GetSpotifyToken -> {
                getSpotifyTokenEvent()
            }
        }
    }

    private fun checkSpotifyPackageEvent() {
        val update = getCurrentViewStateOrNew()
        update.event.checkSpotifyPackage = false
        setViewState(update)
    }

    fun finishCheckSpotifyPackage(done: Boolean) {
        val update = getCurrentViewStateOrNew()
        update.event.checkSpotifyPackage = done
        setViewState(update)
    }

    private fun startFirebaseAuthenticationEvent() {
        val update = getCurrentViewStateOrNew()
        update.event.startFirebaseAuthentication = false
        setViewState(update)
    }

    fun finishFirebaseAuthentication(done: Boolean) {
        val update = getCurrentViewStateOrNew()
        update.event.startFirebaseAuthentication = done
        setViewState(update)
    }

    private fun getSpotifyTokenEvent() {
        val update = getCurrentViewStateOrNew()
        update.event.getSpotifyToken = false
        setViewState(update)
    }

    fun finishSpotifyTokenEvent(done: Boolean) {
        val update = getCurrentViewStateOrNew()
        update.event.getSpotifyToken = done
        setViewState(update)
    }

    override fun handleNewData(data: AuthViewState) {
        // Nothing
    }
}