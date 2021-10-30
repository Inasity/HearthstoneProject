package com.example.android.hearthstoneproject.network.networkmodel

sealed class ServiceResult<out R> {
    data class Success<out T>(val data: T) : ServiceResult<T>()
    data class Error(val exception: Exception) : ServiceResult<Nothing>()
}
