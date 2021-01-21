package com.example.spotifyclone.framework.presentation.ui.auth

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.Observer
import com.example.spotifyclone.business.domain.state.StateMessageCallback
import com.example.spotifyclone.framework.presentation.components.FirebaseErrorDialog
import com.example.spotifyclone.framework.presentation.components.SplashLogo
import com.example.spotifyclone.framework.presentation.components.SpotifyNotInstalledDialog
import com.example.spotifyclone.framework.presentation.components.SpotifyTokenErrorDialog
import com.example.spotifyclone.framework.presentation.theme.SpotifyCloneTheme
import com.example.spotifyclone.framework.presentation.ui.BaseActivity
import com.example.spotifyclone.framework.presentation.ui.auth.state.AuthStateEvent.*
import com.example.spotifyclone.framework.presentation.ui.main.MainActivity
import com.example.spotifyclone.util.printLogD
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint

//Show Logo

//check if spotify package exists on the device
//if not,
//1) hide logo
//2) Show dialog to download spotify, provide link to play store, if backPressed finish AuthActivity
//if yes, Continue

//check if firebase user instance is present
//if not,
//1) hide Logo
//2) Start firebase authentication process, if backPressed finish AuthActivity
//3) if success, show Logo, continue from onActivityResult
//if yes, Continue

//check if spotify token is present in shared preference
//if not,
//1) open spotifyToken fragment
//2) ask user to get spotify token,
//3) if error, show dialog, if backPressed, finish AuthActivity
//4) else, continue
//if yes, Continue

//if these 3 conditions satisfy, navigate to mainActivity


@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
        setContent {
            SpotifyCloneTheme {

                SplashLogo(isDisplayed = viewModel.splashLogo.value)

                SpotifyNotInstalledDialog(
                    isDisplayed = viewModel.spotifyNotInstalledDialog.value,
                    showDialog = { viewModel.spotifyNotInstalledDialog.value = it },
                    downloadSpotify = { downloadSpotify() },
                    closeApp = { this.finish() })

                FirebaseErrorDialog(
                    isDisplayed = viewModel.firebaseErrorDialog.value,
                    text = viewModel.firebaseErrorMessage.value,
                    showDialog = { viewModel.setFirebaseErrorDialog(it, "") },
                    closeApp = { this.finish() })

                SpotifyTokenErrorDialog(
                    isDisplayed = viewModel.spotifyTokenDialog.value,
                    text = viewModel.spotifyTokenErrorMessage.value,
                    showDialog = { viewModel.setSpotifyTokenErrorDialog(it, "") },
                    closeApp = { this.finish() })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(CheckSpotifyPackage)
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(this, Observer { ViewState ->
            ViewState?.let { viewState ->

                viewState.event.checkSpotifyPackage?.let { event ->
                    if (!event) {
                        checkSpotifyPackage()
                    }
                }

                viewState.event.startFirebaseAuthentication?.let { event ->
                    if (!event) {
                        startFirebaseAuthentication()
                    }
                }

                viewState.event.getSpotifyToken?.let { event ->
                    if (!event) {
                        getSpotifyToken()
                    }
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(this, Observer { boolean ->
            displayProgressBar(boolean)
        })

        viewModel.stateMessage.observe(this, Observer { StateMessage ->
            StateMessage?.let { stateMessage ->
                onResponseReceived(
                    response = stateMessage.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.removeStateMessage()
                        }
                    }
                )
            }
        })

        sessionManager.spotifyApp.value?.let { spotifyApp ->
            sessionManager.firebaseUser.value?.let {
                sessionManager.spotifyToken.value?.let {
                    if (spotifyApp) {
                        navMainActivity()
                    }
                }
            }
        }
    }

    private fun checkSpotifyPackage() {
        try {
            this.packageManager.getPackageInfo(SPOTIFY_PACKAGE_NAME, 0)
            printLogD("checkSpotifyPackage", "Spotify is Installed")
            sessionManager.isSpotifyInstalled(true)
            viewModel.apply {
                finishCheckSpotifyPackage(true)
                setStateEvent(StartFirebaseAuthentication)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            printLogD("checkSpotifyPackage", "Spotify Not Installed")
            viewModel.apply {
                splashLogo.value = false
                spotifyNotInstalledDialog.value = true
            }
        }
    }

    private fun downloadSpotify() {
        try {
            val uri = Uri.parse("market://details")
                .buildUpon()
                .appendQueryParameter("id", SPOTIFY_PACKAGE_NAME)
                .appendQueryParameter("referrer", REFERRER)
                .build()
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (ignored: ActivityNotFoundException) {
            val uri = Uri.parse("https://play.google.com/store/apps/details")
                .buildUpon()
                .appendQueryParameter("id", SPOTIFY_PACKAGE_NAME)
                .appendQueryParameter("referrer", REFERRER)
                .build()
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

    private fun startFirebaseAuthentication() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.AnonymousBuilder().build()
            )
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                FIREBASE_AUTHENTICATION_CODE
            )
        } else {
            sessionManager.setFirebaseUser(currentUser)
            viewModel.finishFirebaseAuthentication(true)
            viewModel.setStateEvent(GetSpotifyToken)
        }
    }

    private fun getSpotifyToken() {
        val request = AuthorizationRequest
            .Builder(
                CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                REDIRECT_URI
            )
            .setShowDialog(false)
            .setCampaign("your-campaign-token")
            .build()

        AuthorizationClient.openLoginActivity(this, SPOTIFY_TOKEN_REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FIREBASE_AUTHENTICATION_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == RESULT_OK) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                sessionManager.setFirebaseUser(currentUser)
                viewModel.finishFirebaseAuthentication(true)
                viewModel.setStateEvent(GetSpotifyToken)
            } else {
                response?.error?.message?.let { message ->
                    viewModel.splashLogo.value = false
                    viewModel.setFirebaseErrorDialog(true, message)
                }
            }
        } else if (requestCode == SPOTIFY_TOKEN_REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            if (response.error != null && response.error.isNotEmpty()) {
                viewModel.setSpotifyTokenErrorDialog(true, response.error)
                printLogD("onActivityResult", response.error)
            }
            response.accessToken?.let { token ->
                sessionManager.setSpotifyToken(token)
                viewModel.finishSpotifyTokenEvent(true)
                printLogD("onActivityResult", token)
            }
        }
    }

    private fun firebaseSignOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                TODO()
            }
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun displayProgressBar(isLoading: Boolean) {
        TODO("Not yet implemented")
    }

    companion object {
        const val CLIENT_ID = "e73054640df64a98ba0b72aca221d3c3"
        const val REDIRECT_URI = "comspotifysdktest://callback"
        const val SPOTIFY_PACKAGE_NAME = "com.spotify.music"
        const val REFERRER =
            "adjust_campaign=$SPOTIFY_PACKAGE_NAME&adjust_tracker=ndjczk&utm_source=adjust_preinstall";
        const val FIREBASE_AUTHENTICATION_CODE = 10
        const val SPOTIFY_TOKEN_REQUEST_CODE = 11
    }
}