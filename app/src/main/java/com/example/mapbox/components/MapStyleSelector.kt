package com.example.mapbox.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.mapbox.model.MapStyle

@Composable
fun MapStyleSelector(
    mapStyle: List<MapStyle>,
    selectedStyle: MapStyle,
    onStyleSelected: (MapStyle) -> Unit,
    showMenu: Boolean,
    onShowMenuChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    FloatingActionButton(onClick = {
        onShowMenuChange(true)
    }, modifier = modifier) {
        Icon(Icons.Default.Map, contentDescription = "")
    }

    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = {
            onShowMenuChange(false)
        }
    ) {

        mapStyle.forEach { style ->
            DropdownMenuItem(
                text = { Text(style.name) },
                onClick = {
                    onStyleSelected(style)
                    onShowMenuChange(false)
                }
            )
        }
    }
}