package com.example.grperformance.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grperformance.data.local.TransactionEntity
import com.example.grperformance.ui.theme.*

@Composable
fun TransactionCard(transaction: TransactionEntity, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = iOSCardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        // ... (The Row/Column logic from our previous List Screen code goes here)
    }
}