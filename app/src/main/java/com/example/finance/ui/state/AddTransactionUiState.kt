package com.example.finance.ui.state

import com.example.finance.ui.model.CategoryUiModel
import com.example.finance.ui.model.TransactionType

data class AddTransactionUiState(
    val isLoading: Boolean = false,
    val type: TransactionType = TransactionType.EXPENSE,
    val amountText: String = "",
    val categories: List<CategoryUiModel> = emptyList(),
    val selectedCategory: CategoryUiModel? = null,
    val isAddingNewCategory: Boolean = false,
    val newCategoryName: String = "",
    val newCategoryColorHex: String = "#42A5F5",
    val comment: String = "",
    val dateText: String = "2026-04-01",
    val errorMessage: String? = null,
    val isSaved: Boolean = false
)