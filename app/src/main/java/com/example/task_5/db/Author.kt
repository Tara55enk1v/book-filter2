package com.example.task_5.db

import androidx.room.*

@Entity()
data class Author(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String){}
