package com.example.spotifyclone.framework.presentation.ui.auth

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.Observer
import com.example.spotifyclone.R
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
        setContentView(R.layout.activity_auth)
        subscribeObservers()
        setContent {
            SpotifyCloneTheme {

                SplashLogo(isDisplayed = viewModel.splashLogo.value)




            }
        }
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(this, Observer { ViewState ->
            ViewState?.let { viewState ->

                //Event will be triggered if value is false
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

        //if spotify is installed and user and token not null...proceed to mainActivity
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
            printLogD("startFirebaseAuthentication", "User is null")
            val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build()
            )
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setLogo(R.mipmap.ic_launcher_round)
                    .setTheme(R.style.Theme_SpotifyClone)
                    .build(),
                AuthActivity.FIREBASE_AUTHENTICATION_CODE
            )
        } else {
            printLogD("startFirebaseAuthentication", "User not null")
            sessionManager.setFirebaseUser(currentUser)
            viewModel.finishFirebaseAuthentication(true)
            viewModel.setStateEvent(GetSpotifyToken)
        }
    }

    private fun getSpotifyToken() {
        if (sessionManager.spotifyToken.value == null) {

            val request = AuthorizationRequest
                .Builder(
                    CLIENT_ID,
                    AuthorizationResponse.Type.TOKEN,
                    REDIRECT_URI
                )
                .setShowDialog(false)
                .setCampaign("your-campaign-token")
                .build()

            AuthorizationClient.openLoginActivity(
                this,
                SPOTIFY_TOKEN_REQUEST_CODE,
                request)

        } else {
            printLogD("getSpotifyToken", "token not null")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FIREBASE_AUTHENTICATION_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == RESULT_OK) {
                printLogD("onActivityResult", "Login Successful")
                val currentUser = FirebaseAuth.getInstance().currentUser
                sessionManager.setFirebaseUser(currentUser)
                viewModel.finishFirebaseAuthentication(true)
                viewModel.setStateEvent(GetSpotifyToken)
            } else {
                if (response == null) {
                    printLogD("onActivityResult", "User cancelled auth flow")
                } else {
                    response.error?.let { error ->
                        viewModel.splashLogo.value = false
                        viewModel.showFirebaseErrorDialog(error.errorCode.toString())
                        printLogD(
                            "onActivityResult",
                            "\n Error code : ${error.errorCode}" +
                                    "\n Error message : ${error.message}" +
                                    "\n Error cause : ${error.cause}" +
                                    "\n Error localizedMessage : ${error.localizedMessage}" +
                                    "\n Error stackTrace : ${error.stackTrace}"
                        )
                    }
                }
            }
        } else if (requestCode == SPOTIFY_TOKEN_REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)

            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    sessionManager.setSpotifyToken(response.accessToken)
                    viewModel.finishSpotifyTokenEvent(true)
                    printLogD("onActivityResult", "Token : ${response.accessToken}")
                }
                AuthorizationResponse.Type.CODE -> {
                    printLogD("onActivityResult", "CODE : ${response.code}")
                    viewModel.showSpotifyTokenErrorDialog(response.code)
                }
                AuthorizationResponse.Type.ERROR -> {
                    printLogD("onActivityResult", "ERROR : ${response.error}")
                    viewModel.showSpotifyTokenErrorDialog(response.error)
                }
                AuthorizationResponse.Type.UNKNOWN -> {
                    printLogD("onActivityResult", "UNKNOWN ERROR")
                    viewModel.showSpotifyTokenErrorDialog("unknown Error")
                }
                AuthorizationResponse.Type.EMPTY -> {
                    printLogD("onActivityResult", "EMPTY RESPONSE")
                    viewModel.showSpotifyTokenErrorDialog("empty")
                }
                else -> {
                    printLogD("onActivityResult", "else UNKNOWN ERROR")
                    viewModel.showSpotifyTokenErrorDialog("else nothing")
                }
            }
        }
    }

    private fun firebaseSignOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnSuccessListener {
                printLogD("firebaseSignOut", "Signed Out")
            }
            .addOnFailureListener { error ->
                printLogD(
                    "firebaseSignOut",
                    "\n Error message : ${error.message}" +
                            "\n Error cause : ${error.cause}" +
                            "\n Error localizedMessage : ${error.localizedMessage}" +
                            "\n Error stackTrace : ${error.stackTrace}"
                )
            }
    }

    private fun clearSpotifyToken() {
        sessionManager.setSpotifyToken(null)
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