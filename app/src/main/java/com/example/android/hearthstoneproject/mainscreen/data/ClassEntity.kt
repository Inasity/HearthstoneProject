package com.example.android.hearthstoneproject.mainscreen.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mainscreen_class_list")
data class ClassEntity (
    @PrimaryKey(autoGenerate = true)
    var classId: Long = 0L,

    @ColumnInfo(name = "class_name")
    var className: String = "",

    @ColumnInfo(name = "class_image")
    var classImage: String = ""
        )