package com.example.finance.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finance.data.remote.RetrofitClient
import com.example.finance.data.repository.FinanceRepository
import com.example.finance.ui.components.TransactionCard
import com.example.finance.ui.model.TransactionType
import com.example.finance.ui.model.TransactionUiModel
import com.example.finance.ui.viewmodel.MainViewModel
import com.example.finance.ui.viewmodel.MainViewModelFactory
import java.time.LocalDate

@Composable
fun MainScreen(
    refreshKey: Int,
    onOpenAddTransaction: () -> Unit
) {
//    val transactions = listOf(
//        TransactionUiModel(
//            transactionId = 1,
//            categoryName = "Еда",
//            categoryColorHex = "#ffc852",
//            type = TransactionType.INCOME,
//            amount = 1200.05,
//            comment = "Перевод на еду от мамы",
//            transactionDate = LocalDate.now().toString()
//        ),
//        TransactionUiModel(
//            transactionId = 2,
//            categoryName = "Транспорт",
//            categoryColorHex = "#42A5F5",
//            type = TransactionType.EXPENSE,
//            amount = 350.0,
//            comment = "Автобус",
//            transactionDate = LocalDate.now().toString()
//        )
//    )

//    val balance = transactions.sumOf {
//        if (it.type == TransactionType.INCOME) it.amount else -it.amount
//    }

    val repository = FinanceRepository(RetrofitClient.api)
    val viewModel: MainViewModel = viewModel(factory =
        MainViewModelFactory(repository)
    )
    val state = viewModel.uiState

    LaunchedEffect(refreshKey) {
        viewModel.loadTransactions()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onOpenAddTransaction) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Добавить"
                )
            }
        }
    ) { padding ->
        MainScreenContent(
            padding = padding,
            balance = state.balance,
            transactions = state.transactions
        )
    }
}

@Composable
private fun MainScreenContent(
    padding: PaddingValues,
    balance: Double,
    transactions: List<TransactionUiModel>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(20.dp)
    ) {
        Text(
            text = "Текущий баланс",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "$balance ₽",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(transactions) { item ->
                TransactionCard(item = item)
            }
        }
    }
}