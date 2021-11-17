package com.example.android.hearthstoneproject.ui.listclasscards.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ListCardsDatabaseDao {

    @Update
    suspend fun update(vararg card: CardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg cards: CardEntity)

    @Query("SELECT * FROM card_list ORDER BY cardId DESC")
    fun getAllCards(): LiveData<List<CardEntity>>

    @Query("DELETE FROM card_list")
    fun deleteAll()

    @Query("SELECT * FROM card_list WHERE card_class = :cardClass")
    fun getCardsByClass(cardClass: String): LiveData<List<CardEntity>>

    @Query("SELECT * FROM card_list WHERE cardId = :key")
    fun getCardWithId(key: String): CardEntity

    @Query("SELECT * FROM card_list WHERE card_name = :key")
    fun getCardsWithName(key: String): LiveData<List<CardEntity>>

    @Query("SELECT * FROM card_list WHERE card_favorited = 1")
    fun getFavoriteCards() : LiveData<List<CardEntity>>
}
