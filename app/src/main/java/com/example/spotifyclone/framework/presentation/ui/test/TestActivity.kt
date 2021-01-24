package com.example.spotifyclone.framework.presentation.ui.test

import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.example.spotifyclone.framework.presentation.components.SplashLogo
import com.example.spotifyclone.framework.presentation.theme.SpotifyCloneTheme
import com.example.spotifyclone.framework.presentation.ui.BaseActivity
import com.example.spotifyclone.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : BaseActivity() {

    private val viewModel: TestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
        setContent {
            SpotifyCloneTheme {

                val logo = remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Button(onClick = { viewModel.setOne() }) {
                        Text(text = "set 1")
                    }
                    Button(onClick = { viewModel.setTwo() }) {
                        Text(text = "set 2")
                    }

                    Button(onClick = { viewModel.viewState.value?.one =+ 1 }) {
                        Text(text = "direct 1")
                    }
                    Button(onClick = { viewModel.viewState.value?.two =+ 1 }) {
                        Text(text = "direct 2")
                    }

                    Button(onClick = { logo.value = true }) {
                        Text(text = "Show Logo")
                    }
                    Button(onClick = { logo.value = false }) {
                        Text(text = "Hide Logo")
                    }
                }

                SplashLogo(isDisplayed = logo.value)

            }
        }
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(this, Observer { ViewState ->
            ViewState?.let { viewState ->

                viewState.one?.let {
                    printLogD("subscribeObservers", it.toString())
                }
                viewState.two?.let {
                    printLogD("subscribeObservers", it.toString())
                }
            }
        })
    }

    override fun displayProgressBar(isLoading: Boolean) {
        TODO("Not yet implemented")
    }
}