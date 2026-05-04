package com.example.finance.data.remote

data class TransactionListItemDto (
    val transactionId: Int,
    val userId: Int,
    val categoryId: Int,
    val categoryName: String,
    val colorHex: String?,
    val type: String,
    val amount: Double,
    val comment: String?,
    val transactionDate: String
)
