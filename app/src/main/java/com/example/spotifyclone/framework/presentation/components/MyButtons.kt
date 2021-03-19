package com.example.spotifyclone.framework.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotifyclone.framework.presentation.theme.green700
import com.example.spotifyclone.framework.presentation.theme.typography

@Composable
fun MyButton(
    isDisplayed: Boolean,
    text: String,
    onClick: () -> Unit
) {
    if (isDisplayed){
        Button(
            onClick = { onClick() },
            colors = ButtonDefaults.buttonColors(backgroundColor = green700),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 100.dp)
                .clip(CircleShape),
        ) {
            Text(
                text = text,
                style = typography.h6.copy(fontSize = 14.sp),
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
        }
    }
}