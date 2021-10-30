package com.example.android.hearthstoneproject.network.repo

import com.example.android.hearthstoneproject.listclasscards.data.CardEntity
import com.example.android.hearthstoneproject.network.networkmodel.HearthStoneCard
import com.example.android.hearthstoneproject.network.networkmodel.HearthStoneResponse
import com.example.android.hearthstoneproject.network.networkmodel.HearthstoneStore
import com.example.android.hearthstoneproject.network.networkmodel.ServiceResult
import com.example.android.hearthstoneproject.network.repoImpl.HearthStoneRepoImpl
import kotlinx.coroutines.CoroutineDispatcher

interface HearthStoneRepo {
    suspend fun fetchHearthStoneClasses(viewModelDispatcher: CoroutineDispatcher)
    : ServiceResult<HearthStoneResponse?>

    suspend fun fetchHearthStoneClassCards(viewModelDispatcher: CoroutineDispatcher, className: String)
    : ServiceResult<List<HearthStoneCard>?>

    suspend fun fetchSingleHearthstoneCard(viewModelDispatcher: CoroutineDispatcher, cardName: String)
    : ServiceResult<List<HearthStoneCard>?>

    suspend fun fetchHearthstoneStores(viewModelDispatcher: CoroutineDispatcher, location: String,
    radius: Int, type: String, key: String)
    : ServiceResult<HearthstoneStore?>



    fun convertHearthstoneCardsToCardEntities(cards: List<HearthStoneCard>): Array<CardEntity> {
        return cards.map {
            CardEntity(
                cardId = it.cardId,
                cardName = it.name,
                cardImage = it.img,
                cardSet = it.cardSet,
                cardType = it.type,
                cardRarity = it.rarity,
                cardText = it.text,
                cardClass = it.playerClass)
        }.toTypedArray()
    }



    companion object {
        fun provideHeartStoneRepo(): HearthStoneRepo {
            return HearthStoneRepoImpl()
        }
    }
}
