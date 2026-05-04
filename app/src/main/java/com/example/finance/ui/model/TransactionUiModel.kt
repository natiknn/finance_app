package com.example.finance.ui.model

data class TransactionUiModel(
    val transactionId: Int,
    val categoryName: String,
    val categoryColorHex: String?,
    val type: TransactionType,
    val amount: Double,
    val comment: String?,
    val transactionDate: String
)

data class CategoryUiModel(
    val categoryId: Int,
    val name: String,
    val type: TransactionType,
    val colorHex: String?
)