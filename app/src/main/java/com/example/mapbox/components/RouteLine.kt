package com.example.mapbox.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation

@Composable
fun RouteLine(
    points: List<Point>
) {

    if (points.isNotEmpty()) {

        PolylineAnnotation(
            points = points
        ) {

            lineColor = Color(0xFF1976D2)
            lineWidth = 6.0
        }
    }
}