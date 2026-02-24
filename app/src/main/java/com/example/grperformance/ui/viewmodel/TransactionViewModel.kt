package com.example.grperformance.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grperformance.data.local.TransactionDao
import com.example.grperformance.data.local.TransactionEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionViewModel(private val dao: TransactionDao) : ViewModel() {

    // 1. Fetch all transactions as a StateFlow
    // The UI will "collect" this and update automatically
    val transactions: StateFlow<List<TransactionEntity>> = dao.getAllTransactions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. Add a new transaction
    fun addTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            dao.insertTransaction(transaction)
        }
    }

    // 3. Delete a single transaction
    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            dao.deleteTransaction(transaction)
        }
    }

    // 4. Wipe everything
    fun deleteAllTransactions() {
        viewModelScope.launch {
            dao.deleteAllTransactions()
        }
    }
}