package com.example.mapbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapbox.screens.HomeScreen
import com.example.mapbox.screens.MapScreen
import com.example.mapbox.screens.NavigationScreen
import com.mapbox.geojson.Point

public class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {

                composable("home") {
                    HomeScreen(
                        onOpenMap = {
                            navController.navigate("map")
                        }
                    )
                }

                composable("map") {
                    MapScreen(
                        navController = navController
                    )
                }

                composable("navigation") {

                    val previousEntry = navController.previousBackStackEntry

                    NavigationScreen(
                        origin = Point.fromLngLat(
                            previousEntry
                                ?.savedStateHandle
                                ?.get<Double>("originLng") ?: 0.0,

                            previousEntry
                                ?.savedStateHandle
                                ?.get<Double>("originLat") ?: 0.0
                        ),

                        destination = Point.fromLngLat(
                            previousEntry
                                ?.savedStateHandle
                                ?.get<Double>("destinationLng") ?: 0.0,

                            previousEntry
                                ?.savedStateHandle
                                ?.get<Double>("destinationLat") ?: 0.0
                        ),

                        destinationName =
                            previousEntry
                                ?.savedStateHandle
                                ?.get<String>("destinationName")
                                ?: "Destination"
                    )
                }
            }
        }
    }
}