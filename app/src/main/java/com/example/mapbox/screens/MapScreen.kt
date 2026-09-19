package com.example.mapbox.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import com.mapbox.geojson.Point
import android.widget.Toast
import com.example.mapbox.components.MapSearch
import com.example.mapbox.components.GetLocation
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import android.content.ClipData
import com.example.mapbox.route.RouteManager
import android.content.ClipboardManager
import android.content.Intent
import com.example.mapbox.components.CurrentLocationButton
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
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
import androidx.navigation.NavHostController
import com.example.mapbox.components.LocationMarker
import com.example.mapbox.model.mapStyles
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.delay
import com.example.mapbox.components.MapStyleSelector
import com.example.mapbox.components.MyLocationButton
import com.example.mapbox.components.RouteLine
import com.example.mapbox.components.ZoomControls
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView

@Composable
fun MapScreen(
    navController: NavHostController
) {

    val context = LocalContext.current

    var selectedLocation by remember {
        mutableStateOf<Point?>(null)
    }

    var searchedLocation by remember {
        mutableStateOf<Point?>(null)
    }



    var currentLocation by remember {
        mutableStateOf<Point?>(null)
    }

    var routeDistance by remember {
        mutableStateOf<Double?>(null)
    }

    var routeDuration by remember {
        mutableStateOf<Double?>(null)
    }

    var routeInfo by remember {
        mutableStateOf<com.example.mapbox.route.RouteInfo?>(null)
    }

    var locationName by remember {
        mutableStateOf("")
    }

    var mapView by remember {
        mutableStateOf<MapView?>(null)
    }


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

    val routeManager = remember {
        RouteManager(context)
    }

    LaunchedEffect(searchedLocation) {

        searchedLocation?.let { point ->

            mapViewportState.flyTo(
                CameraOptions.Builder()
                    .center(point)
                    .zoom(15.0)
                    .build()
            )
        }
    }


    LaunchedEffect(searchedLocation, hasLocationPermission) {

        if (searchedLocation != null && !hasLocationPermission) {

            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(currentLocation, searchedLocation) {

        val origin = currentLocation
        val destination = searchedLocation

        if (origin != null && destination != null) {

            routeInfo = routeManager.getRoute(
                origin = origin,
                destination = destination
            )

            routeInfo?.let { route ->

                routeDistance = route.distance
                routeDuration = route.duration
            }
        }
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

            MapEffect(hasLocationPermission, selectedStyle) { view ->

                mapView = view

                if (hasLocationPermission) {

                    view.location.updateSettings {
                        enabled = true
                    }

                    val locationListener =
                        OnIndicatorPositionChangedListener { point ->

                            currentLocation = point
                        }

                    view.location.addOnIndicatorPositionChangedListener(
                        locationListener
                    )
                }

                view.mapboxMap.loadStyle(
                    selectedStyle.uri
                )
            }

            routeInfo?.let { route ->

                RouteLine(
                    points = route.routeCoordinates
                )
            }

        }

        routeInfo?.let { route ->

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 25.dp)
            ) {

                Column(
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    )
                ) {

                    val distanceKm = route.distance / 1000.0

                    val totalMinutes =
                        (route.duration / 60).toInt()

                    val hours =
                        totalMinutes / 60

                    val minutes =
                        totalMinutes % 60

                    Text(
                        text = if (distanceKm >= 1) {
                            "Distance: %.1f km".format(distanceKm)
                        } else {
                            "Distance: ${route.distance.toInt()} m"
                        }
                    )

                    Text(
                        text = if (hours > 0) {
                            "Estimated time: ${hours}h ${minutes}m"
                        } else {
                            "Estimated time: ${minutes} min"
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {

                            val origin = currentLocation
                            val destination = searchedLocation

                            if (origin != null && destination != null) {

                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("originLng", origin.longitude())

                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("originLat", origin.latitude())

                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("destinationLng", destination.longitude())

                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("destinationLat", destination.latitude())

                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("destinationName", locationName)

                                navController.navigate("navigation")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Start Navigation")
                    }
                }
            }
        }

        mapView?.let { view ->

            searchedLocation?.let { point ->

                LocationMarker(
                    mapView = view,
                    point = point,
                    onMarkerClick = {

                    }
                )
            }
        }

        mapView?.let { view ->

            GetLocation(
                mapView = view,
                onLocationSelected = { point, name ->

                    selectedLocation = point
                    locationName = name
                },
                onLocationCleared = {
                    selectedLocation = null
                    locationName = ""
                }
            )
        }

        mapView?.let { view ->

            selectedLocation?.let { point ->

                LocationMarker(
                    mapView = view,
                    point = point,
                    onMarkerClick = {

                        selectedLocation = point
                    }
                )
            }
        }

        selectedLocation?.let { point ->

            Card(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 100.dp)
            ) {

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {

                    Text(
                        text = locationName
                    )

                    Text(
                        text = "Lat: ${point.latitude()}"
                    )

                    Text(
                        text = "Lng: ${point.longitude()}"
                    )

                    Button(onClick = {

                        val latitude = point.latitude()
                        val longitude = point.longitude()

                        val mapLink =
                            "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"


                        val clipboard =
                            context.getSystemService(
                                android.content.Context.CLIPBOARD_SERVICE
                            ) as ClipboardManager

                        val clip =
                            ClipData.newPlainText(
                                "Location Link",
                                mapLink
                            )

                        clipboard.setPrimaryClip(clip)

                        Toast.makeText(
                            context,
                            "Location link copied",
                            Toast.LENGTH_SHORT
                        ).show()


                    }) {

                        Text("Copy Location Link")
                    }

                    Button(
                        onClick = {

                            val latitude = point.latitude()
                            val longitude = point.longitude()

                            val mapLink =
                                "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"

                            val locationText =
                                "$locationName\n" +
                                        "Latitude: $latitude\n" +
                                        "Longitude: $longitude\n" +
                                        "Map: $mapLink"

                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    locationText
                                )
                            }

                            context.startActivity(
                                Intent.createChooser(
                                    shareIntent,
                                    "Share Location"
                                )
                            )
                        }
                    ) {

                        Text("Share Location")
                    }
                }
            }
        }

        MapSearch(
            onLocationSelected = { point, name ->

                searchedLocation = point
                locationName = name
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(
                    start = 16.dp,
                    top = 20.dp,
                    end = 90.dp
                )
        )



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


        MyLocationButton(
            hasLocationPermission = hasLocationPermission,
            permissionLauncher = permissionLauncher,
            mapViewportState = mapViewportState,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 26.dp, end = 20.dp)
        )

        CurrentLocationButton(
            context = context,
            hasLocationPermission = hasLocationPermission,
            permissionLauncher = permissionLauncher,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    bottom = 90.dp,
                    end = 20.dp
                )
        )


    }


}


// yaha tk hua