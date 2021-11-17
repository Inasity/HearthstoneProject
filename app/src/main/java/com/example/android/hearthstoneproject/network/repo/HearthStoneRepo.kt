package com.example.android.hearthstoneproject.network.repo

import com.example.android.hearthstoneproject.ui.listclasscards.data.CardEntity
import com.example.android.hearthstoneproject.network.data.HearthStoneCard
import com.example.android.hearthstoneproject.network.data.HearthStoneResponse
import com.example.android.hearthstoneproject.network.endpoint.HearthStoneApiEndPoints
import com.example.android.hearthstoneproject.network.networkmodel.ServiceResult
import com.example.android.hearthstoneproject.network.repoImpl.HearthStoneRepoImpl
import kotlinx.coroutines.Dispatchers

interface HearthStoneRepo {
    suspend fun fetchHearthStoneClasses()
    : ServiceResult<HearthStoneResponse?>

    suspend fun fetchHearthStoneClassCards(className: String)
    : ServiceResult<List<HearthStoneCard>?>

    suspend fun fetchSingleHearthstoneCard(cardName: String)
    : ServiceResult<List<HearthStoneCard>?>

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
        fun provideHeartStoneRepo(dispatcher: Dispatchers, retroObject: HearthStoneApiEndPoints): HearthStoneRepo {
            return HearthStoneRepoImpl(dispatcher, retroObject)
        }
    }
}
