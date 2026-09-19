package com.example.mapbox.route

data class DirectionsResponse(
    val routes: List<Route>,
    val code: String
)

data class Route(
    val distance: Double,
    val duration: Double,
    val geometry: Geometry
)

data class Geometry(
    val coordinates: List<List<Double>>,
    val type: String
)