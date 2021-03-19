package com.example.spotifyclone.framework.presentation.ui.test

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class TestViewState(
    var three: MutableState<Int?> = mutableStateOf(null)
)