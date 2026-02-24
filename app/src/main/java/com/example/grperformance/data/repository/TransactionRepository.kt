package com.example.grperformance.data.repository

import com.example.grperformance.data.local.TransactionDao
import com.example.grperformance.data.local.TransactionEntity
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {
    val allTransactions: Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()

    suspend fun insert(transaction: TransactionEntity) = transactionDao.insertTransaction(transaction)

    suspend fun delete(transaction: TransactionEntity) = transactionDao.deleteTransaction(transaction)

    suspend fun deleteAll() = transactionDao.deleteAllTransactions()
}