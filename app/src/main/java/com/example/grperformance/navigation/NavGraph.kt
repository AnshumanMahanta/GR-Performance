package com.example.grperformance.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.grperformance.ui.screens.list.TransactionListScreen
import com.example.grperformance.ui.screens.add.AddTransactionScreen
import com.example.grperformance.ui.viewmodel.TransactionViewModel

/**
 * The central navigation hub for the Travel Allowance Logs app.
 * It connects the ViewModel state to the UI screens.
 */
@Composable
fun NavGraph(viewModel: TransactionViewModel) {
    val navController = rememberNavController()

    // Collecting the StateFlow from ViewModel as a Compose State
    // This ensures the UI updates whenever the database changes.
    val transactions by viewModel.transactions.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        // HOME PAGE: Travel Allowance Logs
        composable("list") {
            TransactionListScreen(
                transactions = transactions,
                onAddClick = {
                    navController.navigate("add")
                },
                onDeleteOne = { transaction ->
                    viewModel.deleteTransaction(transaction)
                },
                onDeleteAll = {
                    viewModel.deleteAllTransactions()
                }
            )
        }

        // ADD TRANSACTION PAGE
        composable("add") {
            AddTransactionScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = { transaction ->
                    // 1. Save to Room via ViewModel
                    viewModel.addTransaction(transaction)
                    // 2. Return to the list screen
                    navController.popBackStack()
                }
            )
        }
    }
}