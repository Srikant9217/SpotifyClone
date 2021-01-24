package com.example.spotifyclone.framework.datasource.session

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.spotifyclone.framework.datasource.preferances.MyPreferences
import com.example.spotifyclone.util.printLogD
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    private val preferences: MyPreferences,
) {
    private val _spotifyApp = MutableLiveData(false)
    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    private val _spotifyToken = MutableLiveData<String?>(preferences.getSpotifyToken())

    val spotifyApp: LiveData<Boolean>
        get() = _spotifyApp

    val firebaseUser: LiveData<FirebaseUser?>
        get() = _firebaseUser

    val spotifyToken: LiveData<String?>
        get() = _spotifyToken

    fun isSpotifyInstalled(value: Boolean) {
        if (_spotifyApp.value != value) {
            _spotifyApp.value = value
            printLogD("SessionManager", "Spotify installed : $value")
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
        if (savedToken != token){
            preferences.saveSpotifyToken(token)
            printLogD("SessionManager", "Shared preferences Spotify token Updated : $token")
        }
    }
}