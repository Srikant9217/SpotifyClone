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
import com.example.spotifyclone.R
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.spotifyclone.framework.presentation.components.CircularIndeterminateProgressBar
import com.example.spotifyclone.framework.presentation.components.MyButton
import com.example.spotifyclone.framework.presentation.components.SpotifyNotInstalledDialog
import com.example.spotifyclone.framework.presentation.theme.SpotifyCloneTheme
import com.example.spotifyclone.framework.presentation.ui.BaseFragment
import com.example.spotifyclone.util.printLogD
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SpotifyInstallFragment : BaseFragment() {

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
                            text = "SpotifyInstallFragment",
                            color = MaterialTheme.colors.onBackground
                        )

                        MyButton(
                            isDisplayed = viewModel.spotifyInstallEvent.value == true,
                            text = "Next",
                            onClick = {
                                if (viewModel.sessionManager.firebaseUser.value == null) {
                                    findNavController().navigate(R.id.action_spotifyInstallFragment_to_firebaseAuthFragment)
                                } else {
                                    findNavController().navigate(R.id.action_spotifyInstallFragment_to_spotifyTokenFragment)
                                }
                            }
                        )
                    }

                    SpotifyNotInstalledDialog(
                        isDisplayed = viewModel.spotifyInstallDialog.value,
                        showDialog = { viewModel.spotifyInstallDialog.value = it },
                        downloadSpotify = {
                            uiCommunicationListener.execute(INSTALL_SPOTIFY_EVENT)
                        },
                        backToLauncher = {
                            findNavController().navigate(R.id.action_spotifyInstallFragment_to_launcherFragment)
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(1000)
            uiCommunicationListener.execute(CHECK_SPOTIFY_PACKAGE_EVENT)
        }
    }

    private fun subscribeObservers() {
        viewModel.sessionManager.spotifyInstalled.observe(viewLifecycleOwner, { SpotifyInstalled ->
            SpotifyInstalled?.let { spotifyInstalled ->
                viewModel.hideProgressBar()
                if (spotifyInstalled) {
                    viewModel.spotifyInstallEvent.value = true
                } else {
                    viewModel.spotifyInstallEvent.value = false
                    viewModel.spotifyInstallDialog.value = true
                }
            }
        })
    }

    companion object {
        const val CHECK_SPOTIFY_PACKAGE_EVENT = "CHECK_SPOTIFY_PACKAGE_EVENT"
        const val INSTALL_SPOTIFY_EVENT = "INSTALL_SPOTIFY_EVENT"
    }
}