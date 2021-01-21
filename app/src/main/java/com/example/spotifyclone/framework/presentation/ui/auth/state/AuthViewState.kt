package com.example.spotifyclone.framework.presentation.ui.auth.state

data class AuthViewState(
    var event : Event = Event()
){
    //null -> not triggered
    //false -> triggered, but not completed
    //true -> completed
    data class Event(
        var checkSpotifyPackage: Boolean? = null,
        var startFirebaseAuthentication: Boolean? = null,
        var getSpotifyToken: Boolean? = null
    )
}