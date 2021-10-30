package com.example.android.hearthstoneproject.listclasscards.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "card_list")
data class CardEntity (
    @PrimaryKey(autoGenerate = false)
    var cardId: String,

    @ColumnInfo(name = "card_name")
    var cardName: String? = "",

    @ColumnInfo(name = "card_image")
    var cardImage: String? = "",

    @ColumnInfo(name = "card_set")
    var cardSet: String? = "",

    @ColumnInfo(name = "card_type")
    var cardType: String? = "",

    @ColumnInfo(name = "card_rarity")
    var cardRarity: String? = "",

    @ColumnInfo(name = "card_text")
    var cardText: String? = "",

    @ColumnInfo(name = "card_class")
    var cardClass: String? = "",

    @ColumnInfo(name = "card_favorited")
    var cardFavorited: Boolean = false
        ): Parcelable