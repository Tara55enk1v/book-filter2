package com.example.task_5.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookDao {

    @Query("SELECT * FROM book WHERE title = :title LIMIT 1")
    fun findMovieByTitle(title: String?): Book

    @Query("SELECT * FROM book WHERE authorId = :authorId")
    fun findMovieByAuthorId(authorId: Long): List<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg books: Book)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(book: Book)

    @Query("DELETE FROM book")
    fun deleteAll()

    @Query("""SELECT * from book JOIN author on author.id = book.authorId
            WHERE author.fullName = :author""")
    fun findBooksByAuthor(author: String): List<Book>

    @get:Query("SELECT * FROM book ORDER BY title ASC")
    val allMovies: LiveData<List<Book>>
}