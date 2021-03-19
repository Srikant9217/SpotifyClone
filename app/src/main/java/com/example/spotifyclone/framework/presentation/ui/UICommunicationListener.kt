package com.example.spotifyclone.framework.presentation.ui

import com.example.spotifyclone.business.domain.state.Response
import com.example.spotifyclone.business.domain.state.StateMessageCallback

interface UICommunicationListener {

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )

    fun execute(event: String)
}