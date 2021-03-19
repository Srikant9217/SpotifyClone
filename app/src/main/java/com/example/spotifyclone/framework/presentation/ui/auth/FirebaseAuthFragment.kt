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
import com.example.spotifyclone.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.spotifyclone.framework.presentation.components.FirebaseErrorDialog
import com.example.spotifyclone.framework.presentation.components.MyButton
import com.example.spotifyclone.framework.presentation.theme.SpotifyCloneTheme
import com.example.spotifyclone.framework.presentation.ui.BaseFragment
import com.example.spotifyclone.util.printLogD

class FirebaseAuthFragment : BaseFragment() {

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
                        verticalArrangement = Arrangement.spacedBy(
                            10.dp,
                            Alignment.CenterVertically
                        )
                    ) {
                        Text(
                            text = "FirebaseAuthFragment",
                            color = MaterialTheme.colors.onBackground
                        )

                        MyButton(
                            isDisplayed = viewModel.firebaseAuthEvent.value == null,
                            text = "Login",
                            onClick = {
                                uiCommunicationListener.execute(START_FIREBASE_AUTHENTICATION_EVENT)
                            }
                        )

                        MyButton(
                            isDisplayed = viewModel.firebaseAuthEvent.value == true,
                            text = "Next",
                            onClick = {
                                findNavController().navigate(R.id.action_firebaseAuthFragment_to_spotifyTokenFragment)
                            }
                        )
                    }

                    FirebaseErrorDialog(
                        isDisplayed = viewModel.firebaseErrorDialog.value,
                        text = viewModel.firebaseErrorMessage.value,
                        showDialog = { viewModel.firebaseErrorDialog.value = it },
                        backToLauncher = {
                            findNavController().navigate(R.id.action_firebaseAuthFragment_to_launcherFragment)
                        }
                    )

                }
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.sessionManager.firebaseUser.observe(viewLifecycleOwner, { FirebaseUser ->
            FirebaseUser?.let {
                viewModel.firebaseAuthEvent.value = true
            }
        })
    }

    companion object {
        const val START_FIREBASE_AUTHENTICATION_EVENT = "START_FIREBASE_AUTHENTICATION_EVENT"
    }
}