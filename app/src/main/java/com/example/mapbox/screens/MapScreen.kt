package com.example.mapbox.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.mapbox.model.mapStyles
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.delay
import com.example.mapbox.components.MapStyleSelector
import com.example.mapbox.components.ZoomControls

@Composable
fun MapScreen() {

    val context = LocalContext.current

    var selectedStyle by remember {
        mutableStateOf(mapStyles[0])
    }

    var showStyleMenu by remember {
        mutableStateOf(false)
    }

    var backPressedOnce by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(backPressedOnce) {

        if (backPressedOnce) {
            delay(2000)
            backPressedOnce = false
        }
    }

    BackHandler {

        if (backPressedOnce) {

            //exit the app
            (context as? Activity)?.finishAffinity()
        } else {
            backPressedOnce = true

            Toast.makeText(context, "Press back once again to exit", Toast.LENGTH_SHORT).show()

        }
    }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(2.0)
            pitch(0.0)
            bearing(0.0)
        }
    }

    var hasLocationPermission by remember {

        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )

    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        hasLocationPermission =
            permission[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }




    Box(modifier = Modifier.fillMaxSize()) {


        MapboxMap(
            Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            scaleBar = {
                ScaleBar(
                    Modifier.padding(60.dp)
                )
            },
            logo = {
                Logo(Modifier.padding(bottom = 40.dp))
            },
            attribution = {
                Attribution(Modifier.padding(bottom = 40.dp))
            }

        ) {

            MapEffect(hasLocationPermission, selectedStyle) { mapView ->

                // for fetching the location
                if (hasLocationPermission) {
                    mapView.location.updateSettings {
                        enabled = true
                    }
                }

                // map style
                mapView.mapboxMap.loadStyle(
                    selectedStyle.uri
                )

            }

        }

        ZoomControls(
            mapViewportState = mapViewportState,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 150.dp, end = 20.dp)
        )


        MapStyleSelector(
            mapStyle = mapStyles,
            selectedStyle = selectedStyle,
            onStyleSelected = {
                selectedStyle = it
            },
            showMenu = showStyleMenu,
            onShowMenuChange = {
                showStyleMenu = it
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(
                    end = 20.dp,
                    top = 40.dp
                )
        )


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
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 26.dp, end = 20.dp)
        ) {

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "My Location"
            )

        }


    }


}