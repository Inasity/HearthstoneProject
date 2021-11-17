package com.example.android.hearthstoneproject.network.endpoint

import com.example.android.hearthstoneproject.network.data.HearthstoneStore
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsApiEndPoint {
    @GET("/maps/api/place/nearbysearch/json")
    suspend fun getStores(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") key: String
    ): Response<HearthstoneStore?>
}
