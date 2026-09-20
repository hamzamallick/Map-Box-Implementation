package com.example.mapbox

import android.app.Application
import com.mapbox.common.MapboxOptions

import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp


class MapboxApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        MapboxOptions.accessToken =
            getString(R.string.mapbox_access_token)

        if (!MapboxNavigationApp.isSetup()) {

            MapboxNavigationApp.setup {

                NavigationOptions.Builder(this)
                    .build()
            }
        }

        try {

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}