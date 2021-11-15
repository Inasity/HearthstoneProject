package com.example.android.hearthstoneproject.maps

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.hearthstoneproject.network.networkmodel.HearthstoneStore
import com.example.android.hearthstoneproject.network.networkmodel.ServiceResult
import com.example.android.hearthstoneproject.network.repo.HearthStoneRepo
import com.example.android.hearthstoneproject.secret.API.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MapsViewModel(application: Application, val repo: HearthStoneRepo)
    : AndroidViewModel(application){

    private val _storeFeed = MutableLiveData<HearthstoneStore?>()
    val storeFeed: LiveData<HearthstoneStore?>
        get() = _storeFeed

    fun getStores(location: String, radius: Int, type: String,
                  key: String = API_KEY) {

        val dispatcher = Dispatchers.IO


        viewModelScope.launch(dispatcher) {
            when(val response = repo.fetchHearthstoneStores(dispatcher,
                location = location, radius = radius, type = type, key = key)) {
                is ServiceResult.Success -> {
                    _storeFeed.postValue(response.data)
                    Timber.d("Got the stores baby. " + response.data?.results)
                }

                is ServiceResult.Error -> {
                    Timber.d("Error was found when calling Heartstone stores :: " + response.exception)
                }

                else -> {
                    Timber.d("Uninstall life please")
                }
            }
        }
    }
}
