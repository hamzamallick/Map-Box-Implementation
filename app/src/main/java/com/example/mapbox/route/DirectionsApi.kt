package com.example.mapbox.route

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DirectionsApi {

    @GET("directions/v5/mapbox/driving/{coordinates}")
    suspend fun getRoute(
        @Path("coordinates") coordinates: String,
        @Query("alternatives") alternatives: Boolean = false,
        @Query("geometries") geometries: String = "geojson",
        @Query("overview") overview: String = "full",
        @Query("steps") steps: Boolean = true,
        @Query("access_token") accessToken: String
    ): DirectionsResponse
}