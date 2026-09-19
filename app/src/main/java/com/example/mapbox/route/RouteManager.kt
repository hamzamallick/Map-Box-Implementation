package com.example.mapbox.route

import android.content.Context
import com.mapbox.geojson.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RouteManager(
    private val context: Context
) {

    suspend fun getRoute(
        origin: Point,
        destination: Point
    ): RouteInfo? {

        return withContext(Dispatchers.IO) {

            try {

                val coordinates =
                    "${origin.longitude()},${origin.latitude()};" +
                            "${destination.longitude()},${destination.latitude()}"

                val accessToken =
                    context.getString(
                        com.example.mapbox.R.string.mapbox_access_token
                    )

                val response =
                    RetrofitInstance.directionsApi.getRoute(
                        coordinates = coordinates,
                        accessToken = accessToken
                    )

                val route = response.routes.firstOrNull()
                    ?: return@withContext null

                RouteInfo(
                    origin = origin,
                    destination = destination,
                    distance = route.distance,
                    duration = route.duration,
                    routeCoordinates = route.geometry.coordinates.map { coordinate ->

                        Point.fromLngLat(
                            coordinate[0],
                            coordinate[1]
                        )
                    }
                )

            } catch (e: Exception) {

                e.printStackTrace()

                null
            }
        }
    }
}