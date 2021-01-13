package com.example.spotifyclone.framework.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = green200,
    primaryVariant = green700,
    secondary = teal200,
    background = Color.Black,
    surface = Color.Black,
    onPrimary = Color.Black,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Color.Red,
)

//private val LightColorPalette = lightColors(
//    primary = green500,
//    primaryVariant = green700,
//    secondary = teal200,
//    background = Color.White,
//    surface = Color.White,
//    onPrimary = Color.White,
//    onSecondary = Color.Black,
//    onBackground = Color.Black,
//    onSurface = Color.Black
//)

@Composable
fun SpotifyCloneTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = typography,
        shapes = shapes,
        content = content
    )
}