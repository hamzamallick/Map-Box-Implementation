package com.example.mapbox.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState

@Composable
fun ZoomControls(
    mapViewportState: MapViewportState,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FloatingActionButton(onClick = {
            val currentZoom = mapViewportState.cameraState?.zoom ?: 2.0

            mapViewportState.setCameraOptions {
                zoom(currentZoom + 1.0)
            }
        })
        {


            Icon(Icons.Default.Add, contentDescription = "")
        }

        FloatingActionButton(onClick = {
            val currentZoom = mapViewportState.cameraState?.zoom ?: 2.0

            mapViewportState.setCameraOptions {
                zoom(currentZoom - 1.0)
            }
        })
        {
            Icon(Icons.Default.Remove, contentDescription = "")
        }
    }
}