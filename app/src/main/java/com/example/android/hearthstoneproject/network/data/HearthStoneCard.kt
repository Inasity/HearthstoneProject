package com.example.android.hearthstoneproject.network.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HearthStoneCard (
    val cardId: String,
    val name: String?,
    val cardSet: String?,
    val type: String?,
    val rarity: String?,
    val text: String?,
    val playerClass: String?,
    val img: String?,
        ): Parcelable
