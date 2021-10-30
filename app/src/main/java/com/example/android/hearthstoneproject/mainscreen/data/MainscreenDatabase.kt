package com.example.android.hearthstoneproject.mainscreen.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ClassEntity::class], version = 2, exportSchema = false)
abstract class MainscreenDatabase: RoomDatabase() {

    abstract val mainScreenDatabaseDao: MainscreenDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: MainscreenDatabase? = null

        fun getInstance(context: Context): MainscreenDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if(instance == null)
                {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MainscreenDatabase::class.java,
                        "mainscreen_class_database"
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