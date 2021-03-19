package com.example.spotifyclone.framework.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.example.spotifyclone.R
import com.example.spotifyclone.framework.presentation.ui.BaseActivity
import com.example.spotifyclone.framework.presentation.ui.auth.AuthActivity
import com.example.spotifyclone.framework.presentation.ui.main.home.HomeFragment.Companion.NULL_TOKEN
import com.example.spotifyclone.framework.presentation.ui.main.home.HomeFragment.Companion.NULL_USER
import com.example.spotifyclone.util.printLogD
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint

//Show logo at same position from previous Activity, disable activity Animation
//Try to connect to spotify app
//if errors, show Dialog, if back pressed, finish Activity
//else, Open home fragment

//observe
//1) firebase user instance
//2) spotify token
//if these 2 conditions doesn't satisfy, navigate to AuthActivity

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        restoreSession(savedInstanceState)
        subscribeObservers()
    }

    private fun restoreSession(savedInstanceState: Bundle?) {
        savedInstanceState?.getString(AUTH_TOKEN_BUNDLE_KEY)?.let { token ->
            sessionManager.setSpotifyToken(token)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(AUTH_TOKEN_BUNDLE_KEY, sessionManager.spotifyToken.value)
    }

    override fun execute(event: String) {
        when (event) {
            NULL_USER -> {
                firebaseSignOut()
            }
            NULL_TOKEN -> {
                clearSpotifyToken()
            }
        }
    }

    private fun subscribeObservers() {
        sessionManager.firebaseUser.observe(this, { firebaseUser ->
            if (firebaseUser == null) {
                navAuthActivity()
            }
        })

        sessionManager.spotifyToken.observe(this, { spotifyToken ->
            if (spotifyToken == null) {
                navAuthActivity()
            }
        })
    }

    private fun firebaseSignOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnSuccessListener {
                sessionManager.setFirebaseUser(null)
            }
            .addOnFailureListener { error ->
                printLogD(
                    "MainActivity",
                    "firebaseSignOut : " +
                            "\n Error message : ${error.message}" +
                            "\n Error cause : ${error.cause}"
                )
            }
    }

    private fun clearSpotifyToken() {
        sessionManager.setSpotifyToken(null)
    }

    private fun navAuthActivity() {
        printLogD("MainActivity", "navAuthActivity : Navigating to AuthActivity")
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        const val AUTH_TOKEN_BUNDLE_KEY = "AUTH_TOKEN_BUNDLE_KEY"
    }
}
