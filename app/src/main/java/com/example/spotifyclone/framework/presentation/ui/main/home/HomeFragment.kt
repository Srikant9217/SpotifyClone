package com.example.spotifyclone.framework.presentation.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import com.example.spotifyclone.framework.presentation.theme.SpotifyCloneTheme
import com.example.spotifyclone.framework.presentation.ui.BaseFragment
import com.example.spotifyclone.framework.presentation.ui.main.home.state.HomeStateEvent.SearchTrackEvent
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
                SpotifyCloneTheme(
                    displayProgressBar = false,
                    displayLogo = false,
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp).fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
                    ) {
                        Text(
                            text = "MainActivity",
                            color = MaterialTheme.colors.onBackground
                        )

                        Button(
                            onClick = { uiCommunicationListener.execute(NULL_USER) }
                        ) {
                            Text(text = "null user")
                        }

                        Button(
                            onClick = { uiCommunicationListener.execute(NULL_TOKEN) }
                        ) {
                            Text(text = "null token")
                        }
                    }
                }
            }
        }
    }

    companion object{
        const val NULL_USER = "NULL_USER"
        const val NULL_TOKEN = "NULL_TOKEN"
    }
}
