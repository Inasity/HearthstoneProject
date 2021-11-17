package com.example.android.hearthstoneproject.di

import com.example.android.hearthstoneproject.network.retrofit.RetrofitFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCoroutines() = Dispatchers

    //Gson

    //Retrofit
    @Singleton
    @Provides
    fun provideRetrofitFactory() = RetrofitFactory
}
