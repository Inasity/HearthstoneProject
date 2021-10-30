package com.example.android.hearthstoneproject.mainscreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.hearthstoneproject.mainscreen.data.ClassEntity
import com.example.android.hearthstoneproject.mainscreen.data.MainscreenDatabaseDao
import com.example.android.hearthstoneproject.network.networkmodel.HearthStoneResponse
import com.example.android.hearthstoneproject.network.networkmodel.HearthstoneStore
import com.example.android.hearthstoneproject.network.networkmodel.ServiceResult
import com.example.android.hearthstoneproject.network.repo.HearthStoneRepo
import kotlinx.coroutines.*

class MainScreenViewModel(application: Application, private val repo: HearthStoneRepo,
                          database: MainscreenDatabaseDao)
    : AndroidViewModel(application) {

    var boolean: Boolean = false

    var searchParameter = ""

    private val _classFeed = MutableLiveData<HearthStoneResponse?>()

    private val _classes : LiveData<List<ClassEntity>> = database.getAllClasses()
    val classes : LiveData<List<ClassEntity>>
            get() = _classes

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun getAllClasses() {
        val dispatcher = Dispatchers.IO

        viewModelScope.launch(dispatcher) {
            when(val response = repo.fetchHearthStoneClasses(dispatcher)) {
                is ServiceResult.Success -> {
                    _classFeed.postValue(response.data)
                    Log.d("Zelda", "Got the classes baby. ${response.data?.classes?.size} classes.")
                    Log.d("Zelda", response.data.toString())
                }

                is ServiceResult.Error -> {
                    Log.d("Zelda","Error was found when calling Heartstone classes :: ${response.exception}")
                }

                else -> {
                    Log.d("Zelda","Oh- oh... You've done fucked up...")
                }
            }
        }
    }

    fun whichFunctionToUse(): Int {
        boolean = _classFeed.value?.classes?.contains(searchParameter) ?: false
        Log.d("Zelda","Boolean is $boolean")
        return if(boolean) {
            2
        } else if (!boolean) {
            1
        } else {
            0
        }
    }

    fun updateSearchParameter(text: CharSequence)
    {
        searchParameter = text.toString()
        Log.d("Zelda", searchParameter)
    }

    //    fun insertTheClasses() {
//        uiScope.launch {
//            val mage = ClassEntity()
//            mage.className = "Mage"
//            mage.classImage = "https://cdn.zeplin.io/61428f4f3f9990546f7892b1/assets/A1441330-E02E-4DCE-9A7F-D3371728A979.png"
//            insert(mage)
//            Log.d("Zelda", "${mage.className} has been inserted.")
//
//            val warlock = ClassEntity()
//            warlock.className = "Warlock"
//            warlock.classImage = "https://cdn.zeplin.io/61428f4f3f9990546f7892b1/assets/0AD521C2-D9BF-41D6-971B-58F1EB903325.png"
//            insert(warlock)
//            Log.d("Zelda", "${warlock.className} has been inserted.")
//
//            val hunter = ClassEntity()
//            hunter.className = "Hunter"
//            hunter.classImage = "https://cdn.zeplin.io/61428f4f3f9990546f7892b1/assets/ABF76CE1-DD3D-430C-AF74-F0E182D18A9A.png"
//            insert(hunter)
//            Log.d("Zelda", "${hunter.className} has been inserted.")
//
//            val druid = ClassEntity()
//            druid.className = "Druid"
//            druid.classImage = "https://cdn.zeplin.io/61428f4f3f9990546f7892b1/assets/913370C7-2F90-41D6-B8A9-6B1F0E2963C8.png"
//            insert(druid)
//
//            val priest = ClassEntity()
//            priest.className = "Priest"
//            priest.classImage = "https://cdn.zeplin.io/61428f4f3f9990546f7892b1/assets/E41F5AED-DBAC-44D3-A943-7A2F367C0BA8.png"
//            insert(priest)
//
//            val rogue = ClassEntity()
//            rogue.className = "Rogue"
//            rogue.classImage = "https://cdn.zeplin.io/61428f4f3f9990546f7892b1/assets/54A1FBC2-12A9-4C93-8CEB-B1D496F95378.png"
//            insert(rogue)
//
//            val paladin = ClassEntity()
//            paladin.className = "Paladin"
//            paladin.classImage = "https://cdn.zeplin.io/61428f4f3f9990546f7892b1/assets/4D6663B8-2CAF-4C07-BFE4-FA187A66BC9F.png"
//            insert(paladin)
//
//            val shaman = ClassEntity()
//            shaman.className = "Shaman"
//            shaman.classImage = "https://cdn.zeplin.io/61428f4f3f9990546f7892b1/assets/C1C461EC-9E4D-4002-9B18-80945BB1056F.png"
//            insert(shaman)
//
//            val warrior = ClassEntity()
//            warrior.className = "Warrior"
//            warrior.classImage = "https://cdn.zeplin.io/61428f4f3f9990546f7892b1/assets/FDF3C175-41B9-43D7-86C6-E8470F993028.png"
//            insert(warrior)
//
//            val demon_hunter = ClassEntity()
//            demon_hunter.className = "Demon Hunter"
//            demon_hunter.classImage = "https://cdn.zeplin.io/61428f4f3f9990546f7892b1/assets/B8414EC9-7A6B-455A-90EE-6DDDC72DE8AD.png"
//            insert(demon_hunter)
//
//
//        }
//    }
}