package com.example.mapbox.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point

@Composable
fun NavigationScreen(
    origin: Point,
    destination: Point,
    destinationName: String
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "Navigation to $destinationName"
        )
    }
}