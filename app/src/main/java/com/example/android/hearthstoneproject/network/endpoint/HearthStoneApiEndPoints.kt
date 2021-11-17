package com.example.android.hearthstoneproject.network.endpoint

import com.example.android.hearthstoneproject.network.data.HearthStoneCard
import com.example.android.hearthstoneproject.network.data.HearthStoneResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface HearthStoneApiEndPoints {
    @GET("/cards/classes/{classType}")
    suspend fun getClasses(
        @Header("x-rapidapi-host") host: String = "omgvamp-hearthstone-v1.p.rapidapi.com",
        @Header("x-rapidapi-key") key: String = "4291a61388msh1f5b018fde8a27bp16e251jsn494b88af4524",
        @Path("classType") classType: String
    ): Response<List<HearthStoneCard>>

    @GET("/info")
    suspend fun getInfo(
        @Header("x-rapidapi-host") host: String = "omgvamp-hearthstone-v1.p.rapidapi.com",
        @Header("x-rapidapi-key") key: String = "4291a61388msh1f5b018fde8a27bp16e251jsn494b88af4524",
    ): Response<HearthStoneResponse>

    @GET("/cards/{cardName}")
    suspend fun getCard(
        @Header("x-rapidapi-host") host: String = "omgvamp-hearthstone-v1.p.rapidapi.com",
        @Header("x-rapidapi-key") key: String = "4291a61388msh1f5b018fde8a27bp16e251jsn494b88af4524",
        @Path("cardName") cardName: String
    ): Response<List<HearthStoneCard>>
}
