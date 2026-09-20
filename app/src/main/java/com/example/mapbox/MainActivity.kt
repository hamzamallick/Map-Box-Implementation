package com.example.mapbox

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapbox.screens.HomeScreen
import com.example.mapbox.screens.MapScreen


class MainActivity : ComponentActivity() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContent {

            val navController =
                rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {


                composable("home") {

                    HomeScreen(
                        onOpenMap = {

                            navController.navigate(
                                "map"
                            )
                        }
                    )
                }




                composable("map") {

                    MapScreen(
                        navController = navController
                    )
                }



            }
        }
    }
}