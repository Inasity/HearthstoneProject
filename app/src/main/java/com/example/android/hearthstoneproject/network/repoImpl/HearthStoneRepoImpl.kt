package com.example.android.hearthstoneproject.network.repoImpl

import android.util.Log
import com.example.android.hearthstoneproject.network.networkmodel.HearthStoneCard
import com.example.android.hearthstoneproject.network.networkmodel.HearthStoneResponse
import com.example.android.hearthstoneproject.network.networkmodel.ServiceResult
import com.example.android.hearthstoneproject.network.endpoint.HearthStoneApiEndPoints
import com.example.android.hearthstoneproject.network.networkmodel.HearthstoneStore
import com.example.android.hearthstoneproject.network.repo.HearthStoneRepo
import com.example.android.hearthstoneproject.network.retrofit.RetrofitFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.lang.Exception

class HearthStoneRepoImpl : HearthStoneRepo {

    private val retroObject = RetrofitFactory.retrofitProvider(
        HearthStoneApiEndPoints::class.java,
        "https://omgvamp-hearthstone-v1.p.rapidapi.com/"
    )

    private val retroStoreObject = RetrofitFactory.retrofitProvider(
        HearthStoneApiEndPoints::class.java,
        "https://maps.googleapis.com/"
    )

    override suspend fun fetchHearthStoneClasses(viewModelDispatcher: CoroutineDispatcher): ServiceResult<HearthStoneResponse?> {
        return withContext(viewModelDispatcher) {
            val dataResponse = retroObject.getInfo()

            if(dataResponse.isSuccessful) {
                ServiceResult.Success(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }

        }
    }

    override suspend fun fetchHearthStoneClassCards(viewModelDispatcher: CoroutineDispatcher, className: String): ServiceResult<List<HearthStoneCard>?> {
        return withContext(viewModelDispatcher) {
            val dataResponse = retroObject.getClasses(classType = className)

            if(dataResponse.isSuccessful) {
                ServiceResult.Success(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }

    }

    override suspend fun fetchSingleHearthstoneCard(viewModelDispatcher: CoroutineDispatcher, cardName: String): ServiceResult<List<HearthStoneCard>?> {
        return withContext(viewModelDispatcher) {
            val dataResponse = retroObject.getCard(cardName = cardName)

            if(dataResponse.isSuccessful) {
                ServiceResult.Success(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }

    override suspend fun fetchHearthstoneStores(viewModelDispatcher: CoroutineDispatcher, location: String,
    radius: Int, type: String, key: String): ServiceResult<HearthstoneStore?> {
        return withContext(viewModelDispatcher) {
            val dataResponse = retroStoreObject.getStores(location = location, radius = radius, type = type, key = key)
            Log.d("Zelda", "Data response: ${dataResponse.raw()}")

            if(dataResponse.isSuccessful) {
                ServiceResult.Success(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }
}

