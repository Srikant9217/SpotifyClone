package com.example.spotifyclone.framework.presentation.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.example.spotifyclone.R
import com.example.spotifyclone.framework.presentation.components.FirebaseErrorDialog
import com.example.spotifyclone.framework.presentation.components.SplashLogo
import com.example.spotifyclone.framework.presentation.components.SpotifyNotInstalledDialog
import com.example.spotifyclone.framework.presentation.theme.SpotifyCloneTheme
import com.example.spotifyclone.framework.presentation.ui.auth.AuthActivity
import com.example.spotifyclone.framework.presentation.ui.auth.state.AuthStateEvent
import com.example.spotifyclone.util.printLogD
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class TestActivity : AppCompatActivity() {

    val currentUser = Firebase.auth.currentUser
    private val dialog = mutableStateOf(false)
    private val dialogMessage = mutableStateOf("")
    private val user = mutableStateOf(currentUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotifyCloneTheme {

                val logo = remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = user.value?.displayName.toString(),
                        color = MaterialTheme.colors.onBackground
                    )

                    Button(onClick = { startFirebaseAuthentication() }) {
                        Text(
                            text = "Start Authentication",
                            style = MaterialTheme.typography.button
                        )
                    }
                    Button(onClick = { firebaseSignOut() }) {
                        Text(
                            text = "Logout",
                            style = MaterialTheme.typography.button
                        )
                    }

                    Button(onClick = { logo.value = true }) {
                        Text(text = "Show Logo")
                    }
                    Button(onClick = { logo.value = false }) {
                        Text(text = "Hide Logo")
                    }
                }

                SplashLogo(isDisplayed = logo.value)

                FirebaseErrorDialog(
                    isDisplayed = dialog.value,
                    text = dialogMessage.value,
                    showDialog = { dialog.value = it },
                    closeApp = { this.finish() })
            }
        }
    }

    private fun startFirebaseAuthentication() {
        val currentUser = Firebase.auth.currentUser
        if (currentUser == null) {
            printLogD("startFirebaseAuthentication", "User is null")
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.FacebookBuilder().build(),
                AuthUI.IdpConfig.AnonymousBuilder().build()
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
        }
    }

    private fun getSpotifyToken() {
        val request = AuthorizationRequest
            .Builder(
                AuthActivity.CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                AuthActivity.REDIRECT_URI
            )
            .setShowDialog(false)
            .setCampaign("your-campaign-token")
            .build()

        AuthorizationClient.openLoginActivity(
            this,
            AuthActivity.SPOTIFY_TOKEN_REQUEST_CODE,
            request
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AuthActivity.FIREBASE_AUTHENTICATION_CODE) {

            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                user.value = FirebaseAuth.getInstance().currentUser
                printLogD("onActivityResult", "Login Successful")
            } else {
                if (response == null) {
                    printLogD("onActivityResult", "User cancelled auth flow")
                } else {
                    response.error?.let { error ->
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

        } else if (requestCode == AuthActivity.SPOTIFY_TOKEN_REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            if (response.error != null && response.error.isNotEmpty()) {
                printLogD("onActivityResult", response.error)
            }
            response.accessToken?.let { token ->
                printLogD("onActivityResult", token)
            }
        }
    }

    private fun firebaseSignOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnSuccessListener {
                printLogD("firebaseSignOut", "Signed Out")
                user.value = Firebase.auth.currentUser
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
}