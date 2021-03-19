package com.example.spotifyclone.framework.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.example.spotifyclone.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.spotifyclone.framework.presentation.components.MyButton
import com.example.spotifyclone.framework.presentation.theme.SpotifyCloneTheme
import com.example.spotifyclone.util.printLogD

class LauncherFragment : Fragment() {

    val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.hideLogo()

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
                            text = "LauncherFragment",
                            color = MaterialTheme.colors.onBackground
                        )

                        MyButton(
                            isDisplayed = true,
                            text = "Welcome",
                            onClick = {
                                when {
                                    viewModel.sessionManager.spotifyInstalled.value != true -> {
                                        findNavController().navigate(R.id.action_launcherFragment_to_spotifyInstallFragment)
                                        viewModel.showProgressBar()
                                    }
                                    viewModel.sessionManager.firebaseUser.value == null -> {
                                        findNavController().navigate(R.id.action_launcherFragment_to_firebaseAuthFragment)
                                    }
                                    else -> {
                                        findNavController().navigate(R.id.action_launcherFragment_to_spotifyTokenFragment)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun subscribeObservers() {
        //You are here because, either firebaseUser or spotifyToken is null
        //First, as always, navigate to check if spotify is installed
    }
}