package com.example.spotifyclone.framework.presentation.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.spotifyclone.framework.presentation.components.CircularIndeterminateProgressBar
import com.example.spotifyclone.framework.presentation.components.SplashLogo

private val DarkColorPalette = darkColors(
    primary = green200,
    primaryVariant = green700,
    secondary = teal200,
    background = Color.Black,
    surface = graySurface,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color.Red,
)

@Composable
fun SpotifyCloneTheme(
    displayProgressBar: Boolean,
    displayLogo: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = typography,
        shapes = shapes
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            content()
            CircularIndeterminateProgressBar(isDisplayed = displayProgressBar, 0f)
            SplashLogo(isDisplayed = displayLogo)
        }
    }
}