package com.example.spotifyclone.framework.datasource.cache.database

import androidx.room.TypeConverter
import com.example.spotifyclone.business.domain.model.album.SimplifiedAlbumObject
import com.example.spotifyclone.business.domain.model.artist.ArtistObject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters{

    @TypeConverter
    fun simplifiedAlbumObjectToString(simplifiedAlbumObject: SimplifiedAlbumObject): String? {
        return Gson().toJson(simplifiedAlbumObject)
    }

    @TypeConverter
    fun stringToSimplifiedAlbumObject(string: String?): SimplifiedAlbumObject? {
        val simplifiedAlbumObject = object : TypeToken<SimplifiedAlbumObject>(){}.type
        return Gson().fromJson(string, simplifiedAlbumObject)
    }

    @TypeConverter
    fun artistObjectToString(artistObject: List<ArtistObject>): String? {
        return Gson().toJson(artistObject)
    }

    @TypeConverter
    fun stringToArtistObject(string: String?): List<ArtistObject>? {
        val artistObjectList = object : TypeToken<List<ArtistObject>>(){}.type
        return Gson().fromJson(string, artistObjectList)
    }
}