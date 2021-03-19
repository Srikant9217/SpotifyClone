package com.example.spotifyclone.framework.datasource.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.spotifyclone.framework.datasource.preferances.MyPreferences
import com.example.spotifyclone.util.printLogD
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    private val preferences: MyPreferences,
) {

    private val _spotifyInstalled = MutableLiveData(
        if (!preferences.isSpotifyInstalled()) {
            null
        } else {
            true
        }
    )
    private val _firebaseUser = MutableLiveData<FirebaseUser?>(Firebase.auth.currentUser)
    private val _spotifyToken = MutableLiveData<String?>(preferences.getSpotifyToken())

    val spotifyInstalled: LiveData<Boolean?>
        get() = _spotifyInstalled

    val firebaseUser: LiveData<FirebaseUser?>
        get() = _firebaseUser

    val spotifyToken: LiveData<String?>
        get() = _spotifyToken

    fun setSpotifyInstalled(value: Boolean) {
        if (_spotifyInstalled.value != value) {
            _spotifyInstalled.value = value
            printLogD("SessionManager", "Spotify installed : $value")
        }

        val savedValue = preferences.isSpotifyInstalled()
        if (savedValue != value) {
            preferences.saveSpotifyInstalled(value)
        }
    }

    fun setFirebaseUser(firebaseUser: FirebaseUser?) {
        if (_firebaseUser.value != firebaseUser) {
            _firebaseUser.value = firebaseUser
            printLogD("SessionManager", "FirebaseUser Updated : $firebaseUser")
        }
    }

    fun setSpotifyToken(token: String?) {
        if (_spotifyToken.value != token) {
            _spotifyToken.value = token
            printLogD("SessionManager", "Current Spotify token Updated : $token")
        }

        val savedToken = preferences.getSpotifyToken()
        if (savedToken != token) {
            preferences.saveSpotifyToken(token)
            printLogD("SessionManager", "Shared preferences Spotify token Updated : $token")
        }
    }
}