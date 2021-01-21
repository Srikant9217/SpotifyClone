package com.example.spotifyclone.framework.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.lifecycle.Observer
import com.example.spotifyclone.framework.presentation.ui.BaseActivity
import com.example.spotifyclone.framework.presentation.ui.auth.AuthActivity
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

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
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

    private fun subscribeObservers(){
        sessionManager.spotifyToken.observe(this, Observer{ token ->
            if(token == null){
                navAuthActivity()
            }
        })
    }

    private fun navAuthActivity(){
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun displayProgressBar(isLoading: Boolean) {
        TODO("Not yet implemented")
    }

    companion object{
        const val AUTH_TOKEN_BUNDLE_KEY = "AUTH_TOKEN_BUNDLE_KEY"
    }
}
