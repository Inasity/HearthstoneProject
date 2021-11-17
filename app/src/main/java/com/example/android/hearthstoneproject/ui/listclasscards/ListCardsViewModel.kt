package com.example.android.hearthstoneproject.ui.listclasscards


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.hearthstoneproject.ui.listclasscards.data.CardEntity
import com.example.android.hearthstoneproject.ui.listclasscards.data.ListCardsDatabaseDao
import com.example.android.hearthstoneproject.network.data.HearthStoneCard
import com.example.android.hearthstoneproject.network.networkmodel.ServiceResult
import com.example.android.hearthstoneproject.network.repo.HearthStoneRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListCardsViewModel @Inject constructor(
    application: Application,
    private val hearthStoneRepo: HearthStoneRepo,
    private val listCardsDatabaseDao: ListCardsDatabaseDao,
    private val dispatchers: Dispatchers
) : AndroidViewModel(application) {

    lateinit var cardEntityList: LiveData<List<CardEntity>>

    lateinit var searchParameter: String
    var useClassSearch: Int = 0


    private suspend fun refreshCards() {
        withContext(dispatchers.IO) {
            when (val cardList = hearthStoneRepo.fetchHearthStoneClassCards(searchParameter)) {
                is ServiceResult.Success -> {
                    updateOrCreate(
                        *hearthStoneRepo.convertHearthstoneCardsToCardEntities(
                            cardList.data as List<HearthStoneCard>
                        )
                    )
                }
            }

        }
    }

    private suspend fun updateOrCreate(vararg cards: CardEntity) {
        cards.forEach {
            val cardEntity = getCard(it.cardId)

            if (cardEntity != null) {
                cardEntity.cardName = it.cardName
                cardEntity.cardImage = it.cardImage
                cardEntity.cardSet = it.cardSet
                cardEntity.cardType = it.cardType
                cardEntity.cardRarity = it.cardRarity
                cardEntity.cardText = it.cardText
                cardEntity.cardClass = it.cardClass
                updateCard(cardEntity)
            } else {
                listCardsDatabaseDao.insertAll(it)
            }
        }
    }

    private suspend fun getCard(key: String): CardEntity {
        return withContext(dispatchers.IO) {
            val card = listCardsDatabaseDao.getCardWithId(key)
            card
        }
    }

    suspend fun updateCard(card: CardEntity) {
        withContext(dispatchers.IO) {
            listCardsDatabaseDao.update(card)
        }
    }

    fun initializeTheViewModel() {
        whichMethodToUse()
        when (useClassSearch) {
            2 -> {
                viewModelScope.launch(dispatchers.IO) {
                    Timber.d("Fetching $searchParameter class cards.")
                    refreshCards()
                }
            }
            1 -> {
                viewModelScope.launch(dispatchers.IO) {
                    Timber.d("Fetching $searchParameter card.")
                    getCard()
                }
            }
            else -> {
                Timber.d("Fetching favorite cards.")
            }
        }
    }


    private fun getCard() {
        viewModelScope.launch(dispatchers.IO) {
            when (val response = hearthStoneRepo.fetchSingleHearthstoneCard(searchParameter)) {
                is ServiceResult.Success -> {
                    updateOrCreate(
                        *hearthStoneRepo.convertHearthstoneCardsToCardEntities(
                            response.data as List<HearthStoneCard>
                        )
                    )

                    Timber.d("Got the " + searchParameter + " card baby. " + response.data)
                }

                is ServiceResult.Error -> {
                    Timber.d("Error was found when calling Heartstone "
                            + searchParameter + " card :: " + response.exception)
                }

                else -> {
                    Timber.d("Oh- oh... You've done fucked up...")
                }
            }
        }
    }

    private fun whichMethodToUse() {
        when (useClassSearch) {
            2 -> {
                cardEntityList = listCardsDatabaseDao.getCardsByClass(searchParameter)
            }
            1 -> {
                cardEntityList = listCardsDatabaseDao.getCardsWithName(searchParameter)
            }
            else -> {
                searchParameter = "Your Favorites"
                cardEntityList = listCardsDatabaseDao.getFavoriteCards()
            }
        }
    }

}
