package com.example.spotifyclone.framework.datasource.preferances

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreferences
@Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = prefs.edit()

    fun saveSpotifyInstalled(isInstalled: Boolean) {
        editor.putBoolean(SPOTIFY_INSTALLED, isInstalled)
        editor.apply()
    }

    fun isSpotifyInstalled(): Boolean {
        return prefs.getBoolean(SPOTIFY_INSTALLED, false)
    }

    fun saveSpotifyToken(token: String?) {
        editor.putString(SPOTIFY_TOKEN, token)
        editor.apply()
    }

    fun getSpotifyToken(): String? {
        return prefs.getString(SPOTIFY_TOKEN, null)
    }

    companion object {
        const val SPOTIFY_INSTALLED = "SPOTIFY_INSTALLED"
        const val SPOTIFY_TOKEN = "SPOTIFY_TOKEN"
    }
}