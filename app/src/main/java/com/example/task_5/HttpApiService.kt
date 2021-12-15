package com.example.task_5

import retrofit2.http.GET

interface HttpApiService {
    @GET("/books")
    suspend fun getBooks():List<BookPrimary>
}