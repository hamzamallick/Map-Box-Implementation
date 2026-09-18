package com.example.mapbox.components


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices

@Composable
fun CurrentLocationButton(
    context: Context,
    hasLocationPermission: Boolean,
    permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    modifier: Modifier = Modifier
) {

    FloatingActionButton(
        onClick = {

            if (!hasLocationPermission) {

                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )

            } else {

                val fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(context)

                if (
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {

                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location ->

                            if (location != null) {

                                val latitude = location.latitude
                                val longitude = location.longitude

                                val mapLink =
                                    "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"

                                val shareIntent =
                                    Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"

                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "My Current Location\n$mapLink"
                                        )
                                    }

                                context.startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        "Share My Location"
                                    )
                                )
                            }
                        }
                }
            }
        },
        modifier = modifier
    ) {

        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = "Share My Current Location"
        )
    }
}