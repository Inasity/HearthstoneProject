package com.example.android.hearthstoneproject.ui.mainscreen.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MainscreenDatabaseDao {

    @Insert
    suspend fun insert(classEntity: ClassEntity)

    @Query("SELECT * from mainscreen_class_list ORDER BY classId DESC")
    fun getAllClasses(): LiveData<List<ClassEntity>>

    @Delete
    fun delete(classEntity: ClassEntity)

    @Query("SELECT * from mainscreen_class_list WHERE classId = :key")
    fun getClassWithId(key: Long): LiveData<ClassEntity>
}
