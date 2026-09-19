package com.example.mapbox.route

import com.mapbox.geojson.Point

data class RouteInfo(
    val origin: Point,
    val destination: Point,
    val distance: Double,
    val duration: Double,
    val routeCoordinates: List<Point>
)