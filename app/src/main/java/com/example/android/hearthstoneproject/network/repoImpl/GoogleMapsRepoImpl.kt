package com.example.android.hearthstoneproject.network.repoImpl

import com.example.android.hearthstoneproject.network.data.HearthstoneStore
import com.example.android.hearthstoneproject.network.endpoint.GoogleMapsApiEndPoint
import com.example.android.hearthstoneproject.network.networkmodel.ServiceResult
import com.example.android.hearthstoneproject.network.repo.GoogleMapsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoogleMapsRepoImpl @Inject constructor(
    private val dispatcher: Dispatchers,
    private val retroObject: GoogleMapsApiEndPoint
) : GoogleMapsRepo {

    override suspend fun fetchHearthstoneStores(
        location: String,
        radius: Int,
        type: String,
        key: String
    ): ServiceResult<HearthstoneStore?> {
        return withContext(dispatcher.IO) {
            val dataResponse = retroObject.getStores(
                location = location,
                radius = radius,
                type = type,
                key = key
            )

            if (dataResponse.isSuccessful) {
                ServiceResult.Success(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }
}
