package com.example.task_5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import com.example.task_5.db.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
            applicationContext,
            CatalogDb::class.java, "database-name"
        ).build()
        val authorDao = db.authorDao()
        val bookDao = db.bookDao()
        val authorView = findViewById<EditText>(R.id.authorEditText)
        val filterBtn = findViewById<Button>(R.id.filterBtn)

        var resultCountView = findViewById<TextView>(R.id.resultCountView)
        var listOutputView = findViewById<TextView>(R.id.listOutputView)

        val myApplication = application as ApiApp
        val httpApiService = myApplication.httpApiService

        CoroutineScope(Dispatchers.IO).launch {
            authorDao.deleteAll()
            bookDao.deleteAll()
            val decodedJsonResult = httpApiService.getBooks()
            val bookList = decodedJsonResult.toList()
            for (it in bookList) {
                val author = Author(0, it.author)
                authorDao.insert(author)
            }
            for (it in bookList) {
                val autId = authorDao.findAuthorByName(it.author).id
                val book = Book(
                    0,
                    it.title,
                    autId,
                )
                bookDao.insert(book)
            }
        }

        filterBtn.setOnClickListener {
            var author = authorView.text?.toString()
            var maskedAuthor = if (author.isNullOrEmpty()) ""
            else author
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            CoroutineScope(Dispatchers.IO).launch {
                val filterBookList = bookDao.findBooksByAuthor(maskedAuthor)
                val booksAsString = StringBuilder("")
                for (item in filterBookList.take(3)) {
                    booksAsString.append("Result:  (${item.id}) ${item.title} \n")
                }
                withContext(Dispatchers.Main) {
                    resultCountView.text = "Results: ${filterBookList.size}"
                    listOutputView.text = booksAsString
                }
            }

        }

    }
}