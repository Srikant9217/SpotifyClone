package com.example.spotifyclone.framework.presentation.ui.auth

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import com.example.spotifyclone.R
import com.example.spotifyclone.framework.presentation.ui.BaseActivity
import com.example.spotifyclone.framework.presentation.ui.auth.FirebaseAuthFragment.Companion.START_FIREBASE_AUTHENTICATION_EVENT
import com.example.spotifyclone.framework.presentation.ui.auth.SpotifyInstallFragment.Companion.CHECK_SPOTIFY_PACKAGE_EVENT
import com.example.spotifyclone.framework.presentation.ui.auth.SpotifyInstallFragment.Companion.INSTALL_SPOTIFY_EVENT
import com.example.spotifyclone.framework.presentation.ui.auth.SpotifyTokenFragment.Companion.GET_SPOTIFY_TOKEN_EVENT
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
        setContentView(R.layout.activity_auth)
    }

    private fun subscribeObservers() {
        //if spotify is installed and user and token are not null...proceed to mainActivity
        if (isSpotifyInstalled()) {
            sessionManager.firebaseUser.observe(this, { firebaseUser ->
                firebaseUser?.let {
                    sessionManager.spotifyToken.observe(this, { spotifyToken ->
                        spotifyToken?.let {
                            navMainActivity()
                        }
                    })
                }
            })
        }
    }

    override fun execute(event: String) {
        when (event) {
            CHECK_SPOTIFY_PACKAGE_EVENT -> {
                printLogD("AuthActivity", "execute : checkSpotifyPackage called")
                checkSpotifyPackage()
            }
            INSTALL_SPOTIFY_EVENT -> {
                printLogD("AuthActivity", "execute : installSpotify called")
                installSpotify()
            }
            START_FIREBASE_AUTHENTICATION_EVENT -> {
                printLogD("AuthActivity", "execute : startFirebaseAuth called")
                startFirebaseAuth()
            }
            GET_SPOTIFY_TOKEN_EVENT -> {
                printLogD("AuthActivity", "execute : getSpotifyToken called")
                getSpotifyToken()
            }
        }
    }

    //Used during startup
    private fun isSpotifyInstalled(): Boolean {
        return try {
            this.packageManager.getPackageInfo(SPOTIFY_PACKAGE_NAME, 0)
            printLogD("AuthActivity", "isSpotifyInstalled : Spotify is Installed")
            true
        } catch (e: PackageManager.NameNotFoundException) {
            printLogD("AuthActivity", "isSpotifyInstalled : Spotify Not Installed")
            sessionManager.setSpotifyInstalled(false)
            false
        }
    }

    //Used during Login flow
    private fun checkSpotifyPackage() {
        try {
            this.packageManager.getPackageInfo(SPOTIFY_PACKAGE_NAME, 0)
            printLogD("AuthActivity", "checkSpotifyPackage : Spotify is Installed")
            sessionManager.setSpotifyInstalled(true)
        } catch (e: PackageManager.NameNotFoundException) {
            printLogD("AuthActivity", "checkSpotifyPackage : Spotify Not Installed")
            sessionManager.setSpotifyInstalled(false)
        }
    }

    private fun installSpotify() {
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

    private fun startFirebaseAuth() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            printLogD("AuthActivity", "startFirebaseAuthentication : User is null")
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
                FIREBASE_AUTHENTICATION_CODE
            )
        } else {
            printLogD("AuthActivity", "startFirebaseAuthentication : User not null")
            sessionManager.setFirebaseUser(currentUser)
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
                request
            )

        } else {
            printLogD("AuthActivity", "getSpotifyToken : token not null")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FIREBASE_AUTHENTICATION_CODE) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                printLogD("AuthActivity", "onActivityResult : Login Successful")
                sessionManager.setFirebaseUser(
                    FirebaseAuth.getInstance().currentUser
                )
            } else {
                if (response == null) {
                    printLogD("AuthActivity", "onActivityResult : User cancelled auth flow")
                } else {
                    response.error?.let { error ->
                        viewModel.showFirebaseErrorDialog(error.errorCode.toString())
                        printLogD(
                            "AuthActivity",
                            "onActivityResult : " +
                                    "\nError code : ${error.errorCode}" +
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
                    printLogD("AuthActivity", "onActivityResult : Token ${response.accessToken}")
                    sessionManager.setSpotifyToken(response.accessToken)
                }
                AuthorizationResponse.Type.CODE -> {
                    printLogD("AuthActivity", "onActivityResult : CODE ${response.code}")
                    viewModel.showSpotifyTokenErrorDialog(response.code)
                }
                AuthorizationResponse.Type.ERROR -> {
                    printLogD("AuthActivity", "onActivityResult : ERROR ${response.error}")
                    viewModel.showSpotifyTokenErrorDialog(response.error)
                }
                AuthorizationResponse.Type.UNKNOWN -> {
                    printLogD("AuthActivity", "onActivityResult : UNKNOWN ERROR")
                    viewModel.showSpotifyTokenErrorDialog("unknown Error")
                }
                AuthorizationResponse.Type.EMPTY -> {
                    printLogD("AuthActivity", "onActivityResult : EMPTY RESPONSE")
                    viewModel.showSpotifyTokenErrorDialog("empty")
                }
                else -> {
                    printLogD("AuthActivity", "onActivityResult : else UNKNOWN ERROR")
                    viewModel.showSpotifyTokenErrorDialog("else nothing")
                }
            }
        }
    }

    private fun navMainActivity() {
        printLogD("AuthActivity", "navMainActivity : Navigating to MainActivity")
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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