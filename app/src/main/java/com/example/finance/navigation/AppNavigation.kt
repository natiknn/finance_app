package com.example.finance.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finance.ui.screens.AddTransactionScreen
import com.example.finance.ui.screens.MainScreen

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object AddTransaction : Screen("add_transaction")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val refreshKey = remember { mutableIntStateOf(0) }

    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(
                refreshKey = refreshKey.intValue,
                onOpenAddTransaction = {
                    navController.navigate(Screen.AddTransaction.route)
                }
            )
        }
        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(
                onSaved = {
                    refreshKey.intValue++
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

    }
}