package com.example.spotifyclone.framework.presentation.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun MyButton(
    isDisplayed: Boolean,
    text: String,
    onClick: () -> Unit
){
    if (isDisplayed){
        Button(onClick = { onClick() }) {
            Text(text = text)
        }
    }
}