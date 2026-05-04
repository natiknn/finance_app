package com.example.finance.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finance.data.remote.RetrofitClient
import com.example.finance.data.repository.FinanceRepository
import com.example.finance.ui.components.AddCategoryBlock
import com.example.finance.ui.model.TransactionType
import com.example.finance.ui.viewmodel.AddTransactionViewModel
import com.example.finance.ui.viewmodel.AddTransactionViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    val repository = FinanceRepository(RetrofitClient.api)
    val viewModel: AddTransactionViewModel = viewModel(factory =
        AddTransactionViewModelFactory(repository)
    )
    val state = viewModel.uiState
    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            viewModel.clearSavedFlag()
            onSaved()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Добавить транзакцию") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row {
                FilterChip(
                    selected = state.type == TransactionType.EXPENSE,
                    onClick = { viewModel.updateType(TransactionType.EXPENSE) },
                    label = { Text("Расход") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = state.type == TransactionType.INCOME,
                    onClick = { viewModel.updateType(TransactionType.INCOME) },
                    label = { Text("Доход") }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = state.amountText,
                onValueChange = viewModel::updateAmount,
                label = { Text("Сумма") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text("Категория")
            Spacer(modifier = Modifier.height(8.dp))

            if (!state.isAddingNewCategory) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = state.selectedCategory?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Выберите категорию") },
                        leadingIcon = state.selectedCategory?.colorHex?.let { hex ->
                            {
                                val color = try {
                                    Color(android.graphics.Color.parseColor(hex))
                                } catch (e: Exception) {
                                    Color.Gray
                                }
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                )
                            }
                        },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        state.categories.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        val color = try {
                                            Color(android.graphics.Color.parseColor(
                                                category.colorHex ?: "#808080"
                                            ))
                                        } catch (e: Exception) {
                                            Color.Gray
                                        }
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(category.name)
                                    }
                                },
                                onClick = {
                                    viewModel.updateSelectedCategory(category)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Добавить новую категорию",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { viewModel.toggleAddNewCategory() }
                )
            } else {
                OutlinedButton(
                    onClick = { viewModel.toggleAddNewCategory() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("К категориям")
                }
                Spacer(modifier = Modifier.height(12.dp))
                AddCategoryBlock(
                    name = state.newCategoryName,
                    colorHex = state.newCategoryColorHex,
                    onNameChange = viewModel::updateNewCategoryName,
                    onColorChange = viewModel::updateNewCategoryColorHex
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = state.comment,
                onValueChange = viewModel::updateComment,
                label = { Text("Комментарий") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = state.dateText,
                onValueChange = viewModel::updateDate,
                label = { Text("Дата (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii)
            )
            Spacer(modifier = Modifier.height(20.dp))
            if (state.errorMessage != null) {
                Text("Ошибка: ${state.errorMessage}", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(12.dp))
            }
            Button(
                onClick = { viewModel.saveTransaction() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                Text(if (state.isLoading) "Сохранение..." else "Сохранить")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Назад")
            }
        }
    }
}
