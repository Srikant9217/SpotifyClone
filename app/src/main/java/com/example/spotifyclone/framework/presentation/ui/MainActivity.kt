package com.example.spotifyclone.framework.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifyclone.R
import com.example.spotifyclone.framework.datasource.preferances.MyPreferences
import com.example.spotifyclone.util.printLogD
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var preferences: MyPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getAuthenticationToken()
    }

    private fun getAuthenticationToken() {
        val request = AuthorizationRequest
            .Builder(
                CLIENT_ID,
                AuthorizationResponse.Type.TOKEN,
                REDIRECT_URI
            )
            .setShowDialog(false)
            .setCampaign("your-campaign-token")
            .build()

        AuthorizationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthorizationClient.getResponse(resultCode, data)
        if (response.error != null && response.error.isNotEmpty()) {
            printLogD("onActivityResult", response.error)
        }
        if (requestCode == AUTH_TOKEN_REQUEST_CODE) {
            response.accessToken?.let { token ->
                preferences.saveToken(token)
                printLogD("onActivityResult", token)
            }
        }
    }

    companion object {
        const val CLIENT_ID = "e73054640df64a98ba0b72aca221d3c3"
        const val REDIRECT_URI = "comspotifysdktest://callback"
        const val AUTH_TOKEN_REQUEST_CODE = 0x10
    }
}
