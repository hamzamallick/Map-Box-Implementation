package com.example.mapbox

import android.app.Application
import androidx.startup.AppInitializer
import com.mapbox.common.MapboxOptions

class MapboxApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MapboxOptions.accessToken = getString(R.string.mapbox_access_token)

        // Manual initialization of Mapbox Search SDK via reflection because the internal Initializer is not being triggered automatically
        try {
            val initializerClass = Class.forName("com.mapbox.search.MapboxSearchSdkInitializerImpl")
            @Suppress("UNCHECKED_CAST")
            val initializer = initializerClass as Class<out androidx.startup.Initializer<Any>>
            AppInitializer.getInstance(this)
                .initializeComponent(initializer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}