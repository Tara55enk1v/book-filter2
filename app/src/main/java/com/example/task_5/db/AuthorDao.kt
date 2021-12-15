package com.example.task_5.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AuthorDao {

    @Query("SELECT * FROM author WHERE id = :id LIMIT 1")
    fun findAuthorById(id: Long): Author

    @Query("SELECT * FROM author WHERE fullName = :fullName LIMIT 1")
    fun findAuthorByName(fullName: String): Author

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(author: Author): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg authors: Author)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(author: Author)

    @Query("DELETE FROM author")
    fun deleteAll()

    @get:Query("SELECT * FROM author ORDER BY fullName ASC")
    val allAuthors: LiveData<List<Author>>
}