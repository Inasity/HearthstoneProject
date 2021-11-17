package com.example.android.hearthstoneproject.network.repo

import com.example.android.hearthstoneproject.network.data.HearthstoneStore
import com.example.android.hearthstoneproject.network.endpoint.GoogleMapsApiEndPoint
import com.example.android.hearthstoneproject.network.networkmodel.ServiceResult
import com.example.android.hearthstoneproject.network.repoImpl.GoogleMapsRepoImpl
import kotlinx.coroutines.Dispatchers

interface GoogleMapsRepo {
    suspend fun fetchHearthstoneStores(
        location: String,
        radius: Int, type: String, key: String
    )
            : ServiceResult<HearthstoneStore?>

    companion object {
        fun provideGoogleMapsRepo(
            dispatcher: Dispatchers,
            retroObject: GoogleMapsApiEndPoint
        ): GoogleMapsRepo {
            return GoogleMapsRepoImpl(dispatcher, retroObject)
        }
    }
}
