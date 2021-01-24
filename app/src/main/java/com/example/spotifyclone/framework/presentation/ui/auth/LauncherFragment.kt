package com.example.spotifyclone.framework.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
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

class LauncherFragment : Fragment() {

    val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SpotifyCloneTheme {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 50.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        MyButton(
                            isDisplayed = true,
                            text = "Start",
                            onClick = {
                                findNavController()
                                    .navigate(R.id.action_launcherFragment_to_spotifyInstallFragment)
                            }
                        )
                    }
                }
            }
        }
    }
}