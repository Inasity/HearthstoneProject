package com.example.android.hearthstoneproject.cardinfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.android.hearthstoneproject.listclasscards.data.CardEntity
import com.example.android.hearthstoneproject.listclasscards.data.ListCardsDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CardInfoViewModel(application: Application,
                        val database: ListCardsDatabaseDao,
                        val card: CardEntity)
    : AndroidViewModel(application) {

    suspend fun updateCard(card: CardEntity) {
        withContext(Dispatchers.IO) {
            database.update(card)
        }
    }

}