package com.example.finance.ui.viewmodel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finance.data.remote.CategoryDto
import com.example.finance.data.remote.TransactionDto
import com.example.finance.data.repository.FinanceRepository
import com.example.finance.ui.model.CategoryUiModel
import com.example.finance.ui.model.TransactionType
import com.example.finance.ui.state.AddTransactionUiState
import com.example.finance.ui.util.Constants
import com.example.myapplication.ui.state.MainUiState
import kotlinx.coroutines.launch

class AddTransactionViewModel(
    private val repository: FinanceRepository
) : ViewModel() {
    var uiState by mutableStateOf(AddTransactionUiState(isLoading = true))
        private set

    init {
        loadCategories()
    }
    fun updateType(type: TransactionType) {
        uiState = uiState.copy(
            type = type,
            selectedCategory = null,
            categories = emptyList(),
            isLoading = true
        )
        loadCategories()
    }
    fun updateAmount(value: String) {
        uiState = uiState.copy(amountText = value)
    }
    fun updateSelectedCategory(category: CategoryUiModel?) {
        uiState = uiState.copy(selectedCategory = category)
    }
    fun updateComment(value: String) {
        uiState = uiState.copy(comment = value)
    }
    fun updateDate(value: String) {
        uiState = uiState.copy(dateText = value)
    }
    fun toggleAddNewCategory() {
        uiState = uiState.copy(isAddingNewCategory = !
        uiState.isAddingNewCategory)
    }
    fun updateNewCategoryName(value: String) {
        uiState = uiState.copy(newCategoryName = value)
    }
    fun updateNewCategoryColorHex(value: String) {
        uiState = uiState.copy(newCategoryColorHex = value)
    }
    fun clearSavedFlag() {
        uiState = uiState.copy(isSaved = false)
    }
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val typeString = uiState.type.name
                val result =
                    repository.getCategories(Constants.TEST_USER_ID, typeString)
                val mapped = result.map {
                    CategoryUiModel(
                        categoryId = it.categoryId ?: 0,
                        name = it.name,
                        type = if (it.type == "INCOME")
                            TransactionType.INCOME else TransactionType.EXPENSE,
                        colorHex = it.colorHex
                    )
                }
                uiState = uiState.copy(
                    isLoading = false,
                    categories = mapped,
                    errorMessage = null
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load categories"
                )
            }
        }
    }
    fun saveTransaction() {
        viewModelScope.launch {
            val amount = uiState.amountText.toDoubleOrNull()
            if (amount == null || amount <= 0.0) {
                uiState = uiState.copy(errorMessage = "Введите корректную сумму")
                return@launch
            }
            if (uiState.dateText.isBlank()) {
                uiState = uiState.copy(errorMessage = "Введите дату")
                return@launch
            }
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val categoryId = if (uiState.isAddingNewCategory) {
                    if (uiState.newCategoryName.isBlank()) {
                        uiState = uiState.copy(
                            isLoading = false,
                            errorMessage = "Введите название новой категории"
                        )
                        return@launch
                    }
                    val createdCategory = repository.createCategory(
                        CategoryDto(
                            userId = Constants.TEST_USER_ID,
                            name = uiState.newCategoryName.trim(),
                            type = uiState.type.name,
                            colorHex = uiState.newCategoryColorHex
                        )
                    )
                    createdCategory.categoryId ?: 0
                } else {
                    val selected = uiState.selectedCategory
                    if (selected == null) {
                        uiState = uiState.copy(
                            isLoading = false,
                            errorMessage = "Выберите категорию"
                        )
                        return@launch
                    }
                    selected.categoryId
                }
                repository.createTransaction(
                    TransactionDto(
                        userId = Constants.TEST_USER_ID,
                        categoryId = categoryId,
                        type = uiState.type.name,
                        amount = amount,
                        comment = uiState.comment.ifBlank { null },
                        transactionDate = uiState.dateText
                    )
                )
                uiState = uiState.copy(
                    isLoading = false,
                    isSaved = true,
                    errorMessage = null
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to save transaction"
                )
            }
        }
    }
}