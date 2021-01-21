package com.example.spotifyclone.framework.presentation.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.spotifyclone.business.domain.state.StateMessageCallback
import com.example.spotifyclone.framework.presentation.theme.SpotifyCloneTheme
import com.example.spotifyclone.framework.presentation.ui.main.BaseFragment
import com.example.spotifyclone.framework.presentation.ui.main.home.state.HomeStateEvent.SearchTrackEvent
import com.example.spotifyclone.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SpotifyCloneTheme {
                    val track = viewModel.track
                    val displayProgressBar = viewModel.displayProgressBar

                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            color = MaterialTheme.colors.onBackground,
                            text = track.value?.name ?: "NOT Found"
                        )
                        Button(
                            onClick = { viewModel.setStateEvent(SearchTrackEvent) }
                        ) {
                            Text(text = "Get Track")
                        }
                        if (displayProgressBar.value){
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { ViewState ->
            ViewState?.let { viewState ->
                viewState.track?.let { currentTrack ->
                    printLogD("subscribeObservers", "track url : ${currentTrack.uri}")
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer { display->
            viewModel.displayProgressBar.value = display
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { StateMessage ->
            StateMessage?.let { stateMessage ->
                uiCommunicationListener.onResponseReceived(
                    response = stateMessage.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.removeStateMessage()
                        }
                    }
                )
            }
        })
    }
}
