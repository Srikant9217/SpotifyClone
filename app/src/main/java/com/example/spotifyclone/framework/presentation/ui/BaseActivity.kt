package com.example.spotifyclone.framework.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import com.example.spotifyclone.business.domain.state.AreYouSureCallback
import com.example.spotifyclone.business.domain.state.Response
import com.example.spotifyclone.business.domain.state.StateMessageCallback
import com.example.spotifyclone.business.domain.state.UIComponentType
import com.example.spotifyclone.framework.datasource.session.SessionManager
import com.example.spotifyclone.util.printLogD
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), UICommunicationListener {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
        when (response.uiComponentType) {

            is UIComponentType.AreYouSureDialog -> {
                response.message?.let {
                    areYouSureDialog(
                        message = it,
                        callback = response.uiComponentType.callback,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is UIComponentType.Toast -> {
                response.message?.let {
                    displayToast(
                        message = it,
                        stateMessageCallback = stateMessageCallback
                    )
                }
            }

            is UIComponentType.Dialog -> {
                displayDialog(
                    response = response,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is UIComponentType.None -> {
                // This would be a good place to send to your Error Reporting
                // software of choice (ex: Firebase crash reporting)
                printLogD("BaseActivity", "onResponseReceived: ${response.message}")
                stateMessageCallback.removeMessageFromStack()
            }
        }
    }

    private fun displayDialog(response: Response, stateMessageCallback: StateMessageCallback) {
        TODO("Not yet implemented")
    }

    private fun displayToast(message: String, stateMessageCallback: StateMessageCallback) {
        TODO("Not yet implemented")
    }

    private fun areYouSureDialog(
        message: String,
        callback: AreYouSureCallback,
        stateMessageCallback: StateMessageCallback
    ) {
        TODO("Not yet implemented")
    }

    abstract override fun displayProgressBar(isLoading: Boolean)
}