package com.example.myapplication.ui.state

import com.example.finance.ui.model.CategoryUiModel
import com.example.finance.ui.model.TransactionType
import com.example.finance.ui.model.TransactionUiModel

data class MainUiState(
    val isLoading: Boolean = false,
    val transactions: List<TransactionUiModel> = emptyList(),
    val balance: Double = 0.0,
    val errorMessage: String? = null
)
