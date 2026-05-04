package com.example.finance.data.remote

import com.example.finance.data.remote.CategoryDto
import com.example.finance.data.remote.TransactionListItemDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FinanceApi {
    @GET("api/categories")
    suspend fun getCategories(
        @Query("userId") userId: Int,
        @Query("type") type: String
    ): List<CategoryDto>
    @POST("api/categories")
    suspend fun createCategory(
        @Body request: CategoryDto
    ): CategoryDto
    @GET("api/transactions")
    suspend fun getTransactions(
        @Query("userId") userId: Int
    ): List<TransactionListItemDto>
    @POST("api/transactions")
    suspend fun createTransaction(
        @Body request: TransactionDto
    ): TransactionListItemDto
}