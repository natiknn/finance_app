package com.example.finance.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance.data.repository.FinanceRepository
import com.example.finance.ui.model.TransactionType
import com.example.finance.ui.model.TransactionUiModel
import com.example.finance.ui.util.Constants
import com.example.myapplication.ui.state.MainUiState
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    var uiState by mutableStateOf(MainUiState(isLoading = true))
        private set

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)

            try {
                val items = repository.getTransactions(Constants.TEST_USER_ID).map {
                    TransactionUiModel(
                        transactionId = it.transactionId,
                        categoryName = it.categoryName,
                        categoryColorHex = it.colorHex,
                        type = if (it.type == "INCOME") {
                            TransactionType.INCOME
                        } else {
                            TransactionType.EXPENSE
                        },
                        amount = it.amount,
                        comment = it.comment,
                        transactionDate = it.transactionDate
                    )
                }

                val balance = items.sumOf {
                    if (it.type == TransactionType.INCOME) it.amount else -it.amount
                }

                uiState = uiState.copy(
                    isLoading = false,
                    transactions = items,
                    balance = balance,
                    errorMessage = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unknown error"
                )
            }
        }
    }
}