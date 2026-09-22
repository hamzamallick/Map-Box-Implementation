package com.example.mapbox.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.OnMapLongClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.search.ApiType
import com.mapbox.search.ResponseInfo
import com.mapbox.search.ReverseGeoOptions
import com.mapbox.search.SearchCallback
import com.mapbox.search.SearchEngine
import com.mapbox.search.SearchEngineSettings
import com.mapbox.search.result.SearchResult

@Composable
fun GetLocation(
    mapView: MapView,
    onLocationSelected: (Point, String) -> Unit,
    onLocationCleared: () -> Unit
) {

    val searchEngine = remember {

        SearchEngine.createSearchEngine(
            apiType = ApiType.GEOCODING,
            settings = SearchEngineSettings()
        )
    }

    DisposableEffect(mapView) {

        val clickListener = OnMapClickListener { point ->
            Log.d(
                "GetLocation",
                "MAP CLICK: ${point.latitude()}, ${point.longitude()}"
            )
            onLocationCleared()
            false
        }

        val longClickListener = OnMapLongClickListener { point ->

            Log.d(
                "GetLocation",
                "MAP LONG CLICK: ${point.latitude()}, ${point.longitude()}"
            )

            onLocationSelected(
                point,
                "Getting location..."
            )

            val options = ReverseGeoOptions(
                center = point,
                limit = 1
            )

            searchEngine.search(
                options,
                object : SearchCallback {

                    override fun onResults(
                        results: List<SearchResult>,
                        responseInfo: ResponseInfo
                    ) {
                        if (results.isNotEmpty()) {
                            val result = results[0]
                            val locationName =
                                result.name ?: "Unknown location"
                            Log.d(
                                "GetLocation",
                                "Location: $locationName"
                            )
                            onLocationSelected(
                                point,
                                locationName
                            )
                        } else {
                            onLocationSelected(
                                point,
                                "Location not found"
                            )
                        }
                    }

                    override fun onError(e: Exception) {

                        Log.e(
                            "GetLocation",
                            "Reverse geocoding error",
                            e
                        )

                        onLocationSelected(
                            point,
                            "Unable to get location"
                        )
                    }
                }
            )
            true
        }

        mapView.gestures.addOnMapClickListener(
            clickListener
        )

        mapView.gestures.addOnMapLongClickListener(
            longClickListener
        )

        onDispose {
            mapView.gestures.removeOnMapClickListener(
                clickListener
            )
            mapView.gestures.removeOnMapLongClickListener(
                longClickListener
            )
        }
    }
}