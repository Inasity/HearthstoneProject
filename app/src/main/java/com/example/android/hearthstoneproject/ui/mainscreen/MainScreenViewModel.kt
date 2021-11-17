package com.example.android.hearthstoneproject.ui.mainscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.hearthstoneproject.ui.mainscreen.data.ClassEntity
import com.example.android.hearthstoneproject.ui.mainscreen.data.MainscreenDatabaseDao
import com.example.android.hearthstoneproject.network.data.HearthStoneResponse
import com.example.android.hearthstoneproject.network.networkmodel.ServiceResult
import com.example.android.hearthstoneproject.network.repo.HearthStoneRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    application: Application,
    private val repo: HearthStoneRepo,
    mainscreenDatabaseDao: MainscreenDatabaseDao,
    private val dispatcher: Dispatchers)
    : AndroidViewModel(application) {

    var boolean: Boolean = false

    var searchParameter = ""

    private val _classFeed = MutableLiveData<HearthStoneResponse?>()

    private val _classes : LiveData<List<ClassEntity>> = mainscreenDatabaseDao.getAllClasses()
    val classes : LiveData<List<ClassEntity>>
            get() = _classes

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getAllClasses() {
        viewModelScope.launch(dispatcher.IO) {
            when(val response = repo.fetchHearthStoneClasses()) {
                is ServiceResult.Success -> {
                    _classFeed.postValue(response.data)
                    Timber.d("Got the classes baby. " + response.data?.classes?.size + " classes.")
                    Timber.d(response.data.toString())
                }

                is ServiceResult.Error -> {
                    Timber.d("Error was found when calling Heartstone classes :: " + response.exception)
                }

                else -> {
                    Timber.d("Oh- oh... You've done fucked up...")
                }
            }
        }
    }

    fun whichFunctionToUse(): Int {
        boolean = _classFeed.value?.classes?.contains(searchParameter) ?: false
        Timber.d("Boolean is $boolean")
        return if (boolean) {
            2
        } else if (!boolean) {
            1
        } else {
            0
        }
    }

    fun updateSearchParameter(text: CharSequence) {
        searchParameter = text.toString()
        Timber.d(searchParameter)
    }
}
