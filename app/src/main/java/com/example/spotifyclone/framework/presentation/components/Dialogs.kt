package com.example.spotifyclone.framework.presentation.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.spotifyclone.util.printLogD

@Composable
fun SpotifyNotInstalledDialog(
    isDisplayed: Boolean,
    showDialog: (Boolean) -> Unit,
    downloadSpotify: () -> Unit,
    retry: () -> Unit
) {
    MyDialog(
        isDisplayed = isDisplayed,
        title = "Could not detect Spotify",
        text = "Please Download Spotify",
        confirmButton = {
            Button(onClick = {
                showDialog(false)
                downloadSpotify()
            }) {
                Text(text = "Get Spotify Free")
            }
        },
        onDismissRequest = { },
        retry = { retry() })
}

@Composable
fun FirebaseErrorDialog(
    isDisplayed: Boolean,
    text: String,
    showDialog: (Boolean) -> Unit,
    retry: () -> Unit
) {
    MyDialog(
        isDisplayed = isDisplayed,
        title = "Error",
        text = text,
        confirmButton = {
            Button(onClick = {
                showDialog(false)
                printLogD("FirebaseErrorDialog", "Retry")
            }) {
                Text(text = "Retry")
            }
        },
        onDismissRequest = { retry() },
        retry = { retry() })
}

@Composable
fun SpotifyTokenErrorDialog(
    isDisplayed: Boolean,
    text: String,
    showDialog: (Boolean) -> Unit,
    retry: () -> Unit
) {
    MyDialog(
        isDisplayed = isDisplayed,
        title = "Error",
        text = text,
        confirmButton = {
            Button(onClick = {
                showDialog(false)
                printLogD("SpotifyTokenErrorDialog", "Retry")
            }) {
                Text(text = "Retry")
            }
        },
        onDismissRequest = { retry() },
        retry = { retry() })
}

@Composable
fun MyDialog(
    isDisplayed: Boolean,
    title: String,
    text: String,
    confirmButton: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
    retry: () -> Unit
) {
    if (isDisplayed) {
        AlertDialog(
            title = { Text(text = title) },
            text = { Text(text = text) },
            confirmButton = { confirmButton() },
            dismissButton = {
                Button(onClick = { retry() }) {
                    Text(text = "Dismiss")
                }
            },
            onDismissRequest = { onDismissRequest() }
        )
    }
}