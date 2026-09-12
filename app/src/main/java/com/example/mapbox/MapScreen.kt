package com.example.mapbox

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import kotlinx.coroutines.delay

@Composable
fun MapScreen() {

    val context = LocalContext.current

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


    MapboxMap(
        Modifier.fillMaxSize(),
        mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(2.0)
                center(Point.fromLngLat(-98.0, 39.5))
                pitch(0.0)
                bearing(0.0)
            }
        },
        scaleBar = {
            ScaleBar(Modifier.padding(top = 60.dp))
        },
        logo = {
            Logo(Modifier.padding(bottom = 40.dp))
        },
        attribution = {
            Attribution(Modifier.padding(bottom = 40.dp))
        }
    )
}