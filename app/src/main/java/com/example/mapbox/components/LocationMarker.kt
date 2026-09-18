package com.example.mapbox.components


import androidx.compose.runtime.Composable
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.runtime.DisposableEffect
import com.example.mapbox.R
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor

@Composable
fun LocationMarker(
    mapView: MapView,
    point: Point,
    onMarkerClick: () -> Unit
) {

    DisposableEffect(mapView, point) {

        val annotationApi = mapView.annotations

        val pointAnnotationManager =
            annotationApi.createPointAnnotationManager()

        pointAnnotationManager.addClickListener { annotation ->
            onMarkerClick()
            true

        }

        val drawable = mapView.context
            .getDrawable(R.drawable.pin)

        val bitmap = drawableToBitmap(
            drawable!!,
            80,
            80
        )

        val pointAnnotationOptions =
            PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(bitmap)
                .withIconAnchor(IconAnchor.BOTTOM)

        pointAnnotationManager.create(
            pointAnnotationOptions
        )

        onDispose {
            pointAnnotationManager.deleteAll()
        }
    }
}

private fun drawableToBitmap(
    drawable: Drawable,
    width: Int,
    height: Int
): Bitmap {

    val bitmap = Bitmap.createBitmap(
        width,
        height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)

    drawable.setBounds(
        0,
        0,
        width,
        height
    )

    drawable.draw(canvas)

    return bitmap
}




