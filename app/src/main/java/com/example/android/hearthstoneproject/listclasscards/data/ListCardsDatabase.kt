package com.example.android.hearthstoneproject.listclasscards.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CardEntity::class], version = 4, exportSchema = false)
abstract class ListCardsDatabase: RoomDatabase() {
    abstract val listCardsDatabaseDao: ListCardsDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: ListCardsDatabase? = null

        fun getInstance(context: Context): ListCardsDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if(instance == null)
                {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ListCardsDatabase::class.java,
                        "listcards_card_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}