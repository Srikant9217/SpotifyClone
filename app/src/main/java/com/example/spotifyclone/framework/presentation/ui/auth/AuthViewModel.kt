package com.example.spotifyclone.framework.presentation.ui.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.spotifyclone.business.domain.state.*
import com.example.spotifyclone.framework.datasource.session.SessionManager
import com.example.spotifyclone.framework.presentation.ui.BaseViewModel
import com.example.spotifyclone.framework.presentation.ui.auth.state.AuthViewState
import com.example.spotifyclone.util.printLogD
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject
constructor(
    val sessionManager: SessionManager
) : BaseViewModel<AuthViewState>() {

    //null -> not triggered
    //false -> failed
    //true -> successful
    val spotifyInstallEvent: MutableState<Boolean?> = mutableStateOf(null)
    val firebaseAuthEvent: MutableState<Boolean?> = mutableStateOf(null)
    val spotifyTokenEvent: MutableState<Boolean?> = mutableStateOf(null)

    // Dialogs
    val spotifyInstallDialog = mutableStateOf(false)
    val firebaseErrorDialog = mutableStateOf(false)
    val spotifyTokenDialog = mutableStateOf(false)

    // Dialog Messages
    val firebaseErrorMessage = mutableStateOf("")
    val spotifyTokenErrorMessage = mutableStateOf("")

    val splashLogo = mutableStateOf(false)
    val progressBar = mutableStateOf(false)

    fun hideLogo() {
        splashLogo.value = false
        printLogD("AuthViewModel", "hideLogo : hideLogo called")
    }

    fun showLogo() {
        splashLogo.value = true
        printLogD("AuthViewModel", "showLogo : showLogo called")
    }

    fun hideProgressBar() {
        progressBar.value = false
        printLogD("AuthViewModel", "hideProgressBar : hideProgressBar called")
    }

    fun showProgressBar() {
        progressBar.value = true
        printLogD("AuthViewModel", "showProgressBar : showProgressBar called")
    }

    fun showFirebaseErrorDialog(message: String) {
        firebaseErrorMessage.value = message
        firebaseErrorDialog.value = true
    }

    fun showSpotifyTokenErrorDialog(message: String) {
        spotifyTokenErrorMessage.value = message
        spotifyTokenDialog.value = true
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        // Nothing
    }

    override fun handleNewData(data: AuthViewState) {
        // Nothing
    }
}