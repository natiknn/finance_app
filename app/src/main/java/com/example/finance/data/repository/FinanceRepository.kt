package com.example.finance.data.repository
import com.example.finance.data.remote.CategoryDto
import com.example.finance.data.remote.FinanceApi
import com.example.finance.data.remote.TransactionDto
import com.example.finance.data.remote.TransactionListItemDto

class FinanceRepository(
    private val api: FinanceApi
) {
    suspend fun getTransactions(userId: Int): List<TransactionListItemDto> {
        return api.getTransactions(userId)
    }
    suspend fun getCategories(userId: Int, type: String): List<CategoryDto> {
        return api.getCategories(userId, type)
    }
    suspend fun createCategory(request: CategoryDto): CategoryDto {
        return api.createCategory(request)
    }
    suspend fun createTransaction(request: TransactionDto):
            TransactionListItemDto {
        return api.createTransaction(request)
    }
}