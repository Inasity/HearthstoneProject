package com.example.android.hearthstoneproject.network.repoImpl

import com.example.android.hearthstoneproject.network.data.HearthStoneCard
import com.example.android.hearthstoneproject.network.data.HearthStoneResponse
import com.example.android.hearthstoneproject.network.endpoint.HearthStoneApiEndPoints
import com.example.android.hearthstoneproject.network.networkmodel.ServiceResult
import com.example.android.hearthstoneproject.network.repo.HearthStoneRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HearthStoneRepoImpl @Inject constructor(
    private val dispatcher: Dispatchers,
    private val retroObject: HearthStoneApiEndPoints
) : HearthStoneRepo {

    override suspend fun fetchHearthStoneClasses(): ServiceResult<HearthStoneResponse?> {
        return withContext(dispatcher.IO) {
            val dataResponse = retroObject.getInfo()

            if(dataResponse.isSuccessful) {
                ServiceResult.Success(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }

        }
    }

    override suspend fun fetchHearthStoneClassCards(className: String): ServiceResult<List<HearthStoneCard>?> {
        return withContext(dispatcher.IO) {
            val dataResponse = retroObject.getClasses(classType = className)

            if(dataResponse.isSuccessful) {
                ServiceResult.Success(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }

    }

    override suspend fun fetchSingleHearthstoneCard(cardName: String): ServiceResult<List<HearthStoneCard>?> {
        return withContext(dispatcher.IO) {
            val dataResponse = retroObject.getCard(cardName = cardName)

            if(dataResponse.isSuccessful) {
                ServiceResult.Success(dataResponse.body())
            } else {
                ServiceResult.Error(Exception(dataResponse.errorBody().toString()))
            }
        }
    }
}

