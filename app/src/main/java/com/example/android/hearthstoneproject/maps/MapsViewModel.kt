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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel(application: Application, val repo: HearthStoneRepo)
    : AndroidViewModel(application){

    private val _storeFeed = MutableLiveData<HearthstoneStore?>()
    val storeFeed: LiveData<HearthstoneStore?>
        get() = _storeFeed

    fun getStores(location: String, radius: Int, type: String,
                  key: String = "AIzaSyCiJxfTYJfP57FLWYGYmmDjikgCpaBhXjs") {

        val dispatcher = Dispatchers.IO


        viewModelScope.launch(dispatcher) {
            when(val response = repo.fetchHearthstoneStores(dispatcher,
                location = location, radius = radius, type = type, key = key)) {
                is ServiceResult.Success -> {
                    _storeFeed.postValue(response.data)
                    Log.d("Zelda", "Got the stores baby. ${response.data?.results}")
                }

                is ServiceResult.Error -> {
                    Log.d("Zelda","Error was found when calling Heartstone stores :: ${response.exception}")
                }

                else -> {
                    Log.d("Zelda","Oh- oh... You've done fucked up...")
                }
            }
        }
    }
}