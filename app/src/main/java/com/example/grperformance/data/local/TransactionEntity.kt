package com.example.grperformance.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val date: Long, // Storing as Long (Timestamp) for easy sorting
    val note: String,
    val participants: List<String> // Who was involved (The 6 members)
)