package com.example.spotifyclone.framework.presentation.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import com.example.spotifyclone.framework.presentation.theme.typography
import com.spotify.protocol.types.Album

@Composable
fun SpotifyHomeGridItem(album: Album) {
    Card(
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable(onClick = {})
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                bitmap = imageResource(id = 1),
                modifier = Modifier.preferredSize(55.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = album.name,
                style = typography.h6.copy(fontSize = 14.sp),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
fun SpotifyLaneItem(album: Album) {
    val context = AmbientContext.current
    val album = remember { album }
    Column(
        modifier =
        Modifier.preferredWidth(180.dp).padding(8.dp)
            .clickable(
                onClick = {
                    //Disclaimer: We should pass event top level and there should startActivity
                    //context.startActivity(SpotifyDetailActivity.newIntent(context, album))
                })
    ) {
        Image(
            bitmap = imageResource(id = 1),
            modifier = Modifier.preferredWidth(180.dp)
                .preferredHeight(160.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "${album.name}: ${album.name}",
            style = typography.body2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
fun SpotifySearchGridItem(album: Album) {
    val imageBitmap = imageResource(id = 1).asAndroidBitmap()
    val swatch = remember(1) { generateDominantColorState(imageBitmap) }
    val dominantGradient =
        remember { listOf(Color(swatch.rgb), Color(swatch.rgb).copy(alpha = 0.6f)) }
    val context = AmbientContext.current
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = {
                //Disclaimer: We should pass event top level and there should startActivity
                //context.startActivity(SpotifyDetailActivity.newIntent(context, album))
            })
            .preferredHeight(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .horizontalGradientBackground(dominantGradient),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = album.name,
            style = typography.h6.copy(fontSize = 14.sp),
            modifier = Modifier.padding(8.dp)
        )
        Image(
            bitmap = imageResource(id = 1),
            contentScale = ContentScale.Crop,
            modifier = Modifier.preferredSize(70.dp)
                .align(Alignment.Bottom)
                .graphicsLayer(translationX = 40f, rotationZ = 32f, shadowElevation = 16f)
        )
    }
}

fun generateDominantColorState(bitmap: Bitmap): Palette.Swatch {
    return Palette.Builder(bitmap)
        .resizeBitmapArea(0)
        .maximumColorCount(16)
        .generate()
        .swatches
        .maxByOrNull { swatch -> swatch.population }!!
}
