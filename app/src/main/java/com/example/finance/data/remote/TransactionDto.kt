package com.example.finance.data.remote

data class TransactionDto(
    val transactionId: Int? = null,
    val userId: Int,
    val categoryId: Int,
    val type: String,
    val amount: Double,
    val comment: String? = null,
    val transactionDate: String
)