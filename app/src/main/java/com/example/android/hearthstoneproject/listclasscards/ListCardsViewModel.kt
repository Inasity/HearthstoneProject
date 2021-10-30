package com.example.android.hearthstoneproject.listclasscards

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.android.hearthstoneproject.listclasscards.data.CardEntity
import com.example.android.hearthstoneproject.listclasscards.data.ListCardsDatabaseDao
import com.example.android.hearthstoneproject.network.networkmodel.HearthStoneCard
import com.example.android.hearthstoneproject.network.networkmodel.ServiceResult
import com.example.android.hearthstoneproject.network.repo.HearthStoneRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListCardsViewModel(application: Application,
                         private val repo: HearthStoneRepo,
                         val database: ListCardsDatabaseDao,
                         var searchParameter: String,
                         val useClassSearch: Int)
    : AndroidViewModel(application) {

    lateinit var cardEntityList: LiveData<List<CardEntity>>


    private suspend fun refreshCards() {
        val dispatcher = Dispatchers.IO

        withContext(dispatcher) {
            when(val cardList = repo.fetchHearthStoneClassCards(dispatcher, searchParameter))
            {
                is ServiceResult.Success -> {
                    updateOrCreate(*repo.convertHearthstoneCardsToCardEntities(
                        cardList.data as List<HearthStoneCard>))
                }
            }

        }
    }

    private suspend fun updateOrCreate(vararg cards: CardEntity) {
        cards.forEach {
            val cardEntity = getCard(it.cardId)

                if(cardEntity != null)
                {
                    cardEntity.cardName = it.cardName
                    cardEntity.cardImage = it.cardImage
                    cardEntity.cardSet = it.cardSet
                    cardEntity.cardType = it.cardType
                    cardEntity.cardRarity = it.cardRarity
                    cardEntity.cardText = it.cardText
                    cardEntity.cardClass = it.cardClass
                    updateCard(cardEntity)
                }

                else
                {
                    database.insertAll(it)
                }
        }
    }

    private suspend fun getCard(key: String): CardEntity {
        return withContext(Dispatchers.IO) {
            val card = database.getCardWithId(key)
            card
        }
    }

    suspend fun updateCard(card: CardEntity) {
        withContext(Dispatchers.IO) {
            database.update(card)
        }
    }

    init {
        whichMethodToUse()
        when (useClassSearch) {
            2 -> {
                viewModelScope.launch {
                    Log.d("Zelda", "Fetching $searchParameter class cards.")
                    refreshCards()
                }
            }
            1 -> {
                viewModelScope.launch {
                    Log.d("Zelda", "Fetching $searchParameter card.")
                    getCard()
                }
            }
            else -> {
                Log.d("Zelda","Fetching favorite cards.")

            }
        }

    }

    private fun getCard() {
        val dispatcher = Dispatchers.IO

        viewModelScope.launch(dispatcher) {
            when(val response = repo.fetchSingleHearthstoneCard(dispatcher, searchParameter)) {
                is ServiceResult.Success -> {
                    updateOrCreate(*repo.convertHearthstoneCardsToCardEntities(
                        response.data as List<HearthStoneCard>))

                    Log.d("Zelda", "Got the $searchParameter card baby. ${response.data}")
                }

                is ServiceResult.Error -> {
                    Log.d("Zelda","Error was found when calling Heartstone $searchParameter card :: ${response.exception}")
                }

                else -> {
                    Log.d("Zelda","Oh- oh... You've done fucked up...")
                }
            }
        }
    }

    private fun whichMethodToUse(){
        when (useClassSearch) {
            2 -> {
                cardEntityList = database.getCardsByClass(searchParameter)
            }
            1 -> {
                cardEntityList = database.getCardsWithName(searchParameter)
            }
            else -> {
                searchParameter = "Your Favorites"
                cardEntityList = database.getFavoriteCards()
            }
        }
    }

}