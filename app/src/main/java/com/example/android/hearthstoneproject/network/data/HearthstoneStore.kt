package com.example.android.hearthstoneproject.network.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HearthstoneStore (
    val results: List<Results>
        ): Parcelable

@Parcelize
data class Results (
    val place_id: String,
    val name: String,
    val geometry: Geometry,
    val price_level: Int,
): Parcelable

@Parcelize
data class Geometry (
    val location: Location
        ): Parcelable

@Parcelize
data class Location (
    val lat: Double,
    val lng: Double
        ): Parcelable
