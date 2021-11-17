package com.example.android.hearthstoneproject.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.hearthstoneproject.network.data.HearthstoneStore
import com.example.android.hearthstoneproject.network.networkmodel.ServiceResult
import com.example.android.hearthstoneproject.network.repo.GoogleMapsRepo
import com.example.android.hearthstoneproject.secret.API.API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val GoogleMapsRepo: GoogleMapsRepo,
    private val dispatchers: Dispatchers
    ) : ViewModel() {

    private val _storeFeed = MutableLiveData<HearthstoneStore?>()
    val storeFeed: LiveData<HearthstoneStore?>
        get() = _storeFeed

    fun getStores(
        location: String, radius: Int, type: String,
        key: String = API_KEY
    ) {
        viewModelScope.launch(dispatchers.IO) {
            when (val response = GoogleMapsRepo.fetchHearthstoneStores(
                location = location, radius = radius, type = type, key = key
            )) {
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
