package com.example.mapbox

data class MapStyle(
    val name: String,
    val uri: String
)

val mapStyles = listOf(
    MapStyle(
        name = "Standard",
        uri = "mapbox://styles/mapbox/standard"
    ),
    MapStyle(
        name = "Satellite",
        uri = "mapbox://styles/mapbox/standard-satellite"
    ),

    MapStyle(
        name = "Streets",
        uri = "mapbox://styles/mapbox/streets-v12"
    ),

    MapStyle(
        name = "Outdoors",
        uri = "mapbox://styles/mapbox/outdoors-v12"
    ),

    MapStyle(
        name = "Light",
        uri = "mapbox://styles/mapbox/light-v11"
    ),

    MapStyle(
        name = "Dark",
        uri = "mapbox://styles/mapbox/dark-v11"
    )
)
