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

    fun saveSpotifyToken(token: String?) {
        val editor = prefs.edit()
        editor.putString(SPOTIFY_TOKEN, token)
        editor.apply()
    }

    fun getSpotifyToken(): String? {
        return prefs.getString(SPOTIFY_TOKEN, null)
    }

    companion object {
        const val SPOTIFY_TOKEN = "SPOTIFY_TOKEN"
    }
}