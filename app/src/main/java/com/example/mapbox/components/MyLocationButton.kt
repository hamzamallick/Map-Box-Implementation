package com.example.mapbox.components

import android.Manifest
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState

@Composable
fun MyLocationButton(
    hasLocationPermission: Boolean,
    permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    mapViewportState: MapViewportState,
    modifier: Modifier = Modifier

) {

    FloatingActionButton(
        onClick = {
            if (hasLocationPermission) {
                mapViewportState.transitionToFollowPuckState()
            } else {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        },
        modifier = modifier

    ) {

        Icon(Icons.Default.LocationOn, contentDescription = "")
    }

}