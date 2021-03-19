package com.example.spotifyclone.framework.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.spotifyclone.R
import com.example.spotifyclone.framework.presentation.components.CircularIndeterminateProgressBar
import com.example.spotifyclone.framework.presentation.components.MyButton
import com.example.spotifyclone.framework.presentation.components.SpotifyTokenErrorDialog
import com.example.spotifyclone.framework.presentation.theme.SpotifyCloneTheme
import com.example.spotifyclone.framework.presentation.ui.BaseFragment
import com.example.spotifyclone.util.printLogD

class SpotifyTokenFragment : BaseFragment() {

    val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        subscribeObservers()
        return ComposeView(requireContext()).apply {
            setContent {
                SpotifyCloneTheme(
                    displayProgressBar = viewModel.progressBar.value,
                    displayLogo = viewModel.splashLogo.value,
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
                    ) {

                        Text(
                            text = "SpotifyTokenFragment",
                            color = MaterialTheme.colors.onBackground
                        )

                        MyButton(
                            isDisplayed = viewModel.spotifyTokenEvent.value == null,
                            text = "Connect to Spotify",
                            onClick = {
                                viewModel.showProgressBar()
                                uiCommunicationListener.execute(GET_SPOTIFY_TOKEN_EVENT)
                            }
                        )
                    }

                    SpotifyTokenErrorDialog(
                        isDisplayed = viewModel.spotifyTokenDialog.value,
                        text = viewModel.spotifyTokenErrorMessage.value,
                        showDialog = { viewModel.spotifyTokenDialog.value = it },
                        backToLauncher = {
                            findNavController().navigate(R.id.action_spotifyTokenFragment_to_launcherFragment)
                        }
                    )
                }
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.sessionManager.spotifyToken.observe(viewLifecycleOwner, { spotifyToken ->
            spotifyToken?.let {
                viewModel.spotifyTokenEvent.value = true
            }
        })
    }

    companion object {
        const val GET_SPOTIFY_TOKEN_EVENT = "GET_SPOTIFY_TOKEN_EVENT"
    }
}