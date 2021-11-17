package com.example.android.hearthstoneproject.di.database

import android.content.Context
import androidx.room.Room
import com.example.android.hearthstoneproject.ui.listclasscards.data.ListCardsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ListCardsDatabaseModule {
    @Singleton
    @Provides
    fun provideListCardsDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        ListCardsDatabase::class.java,
        "listcards_card_database"
    ).build()

    @Singleton
    @Provides
    fun provideListCardsDatabaseDao(db: ListCardsDatabase) = db.listCardsDatabaseDao
}
