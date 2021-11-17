package com.example.android.hearthstoneproject.di.database

import android.content.Context
import androidx.room.Room
import com.example.android.hearthstoneproject.ui.mainscreen.data.MainscreenDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MainScreenDatabaseModule {

    @Singleton
    @Provides
    fun provideMainScreenDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        MainscreenDatabase::class.java,
        "mainscreen_class_database"
    ).build()

    @Singleton
    @Provides
    fun provideMainSCreenDatabaseDao(db: MainscreenDatabase) = db.mainScreenDatabaseDao
}
