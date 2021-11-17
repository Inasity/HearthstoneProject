package com.example.android.hearthstoneproject.di.network

import com.example.android.hearthstoneproject.network.endpoint.HearthStoneApiEndPoints
import com.example.android.hearthstoneproject.network.repo.HearthStoneRepo
import com.example.android.hearthstoneproject.network.repoImpl.HearthStoneRepoImpl
import com.example.android.hearthstoneproject.network.retrofit.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HearthstoneNetworkModule {
    private const val HearthstoneBaseUrl = "https://omgvamp-hearthstone-v1.p.rapidapi.com/"

    @Singleton
    @Provides
    fun provideHearthstoneRetrofit(): HearthStoneApiEndPoints {
        return RetrofitFactory.retrofitProvider(
            HearthStoneApiEndPoints::class.java,
            HearthstoneBaseUrl
        )
    }

    @Singleton
    @Provides
    fun provideHearthstoneRepo(
        dispatcher: Dispatchers,
        retroObject: HearthStoneApiEndPoints
    ): HearthStoneRepo {
        return HearthStoneRepoImpl(dispatcher, retroObject)
    }
}
