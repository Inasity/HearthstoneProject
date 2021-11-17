package com.example.android.hearthstoneproject.ui.cardinfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.hearthstoneproject.ui.listclasscards.data.CardEntity
import com.example.android.hearthstoneproject.ui.listclasscards.data.ListCardsDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CardInfoViewModel @Inject constructor(
    application: Application,
    private val database: ListCardsDatabaseDao,
    private val dispatchers: Dispatchers
    ) : AndroidViewModel(application) {

    lateinit var card: CardEntity

    suspend fun updateCard(card: CardEntity) {
        withContext(dispatchers.IO) {
            database.update(card)
        }
    }

    fun update(){
        viewModelScope.launch(dispatchers.IO) {
            updateCard(card)
        }
    }

}
