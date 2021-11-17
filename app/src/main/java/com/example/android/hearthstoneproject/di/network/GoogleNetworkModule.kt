package com.example.android.hearthstoneproject.di.network

import com.example.android.hearthstoneproject.network.endpoint.GoogleMapsApiEndPoint
import com.example.android.hearthstoneproject.network.repo.GoogleMapsRepo
import com.example.android.hearthstoneproject.network.repoImpl.GoogleMapsRepoImpl
import com.example.android.hearthstoneproject.network.retrofit.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object GoogleNetworkModule {
    private const val GoogleMapsBaseUrl = "https://maps.googleapis.com/"

    @Singleton
    @Provides
    fun provideGoogleMapsRetrofit(): GoogleMapsApiEndPoint {
        return RetrofitFactory.retrofitProvider(
            GoogleMapsApiEndPoint::class.java,
            GoogleMapsBaseUrl
        )
    }

    @Singleton
    @Provides
    fun provideGoogleMapsRepo(dispatcher: Dispatchers, retroObject: GoogleMapsApiEndPoint): GoogleMapsRepo {
        return GoogleMapsRepoImpl(dispatcher, retroObject)
    }
}
