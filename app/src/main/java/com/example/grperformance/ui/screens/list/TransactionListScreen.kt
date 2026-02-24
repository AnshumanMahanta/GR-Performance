package com.example.grperformance.ui.screens.list

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import com.example.grperformance.data.local.TransactionEntity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

val iOSBlue = Color(0xFF2323FF)

@Composable
fun TransactionListScreen(
    transactions: List<TransactionEntity>,
    onAddClick: () -> Unit,
    onDeleteOne: (TransactionEntity) -> Unit,
    onDeleteAll: () -> Unit
) {
    val context = LocalContext.current

    // --- DIALOG STATES ---
    var transactionToDelete by remember { mutableStateOf<TransactionEntity?>(null) }
    var showDeleteAllConfirm by remember { mutableStateOf(false) }

    // Single Delete Dialog
    transactionToDelete?.let { transaction ->
        IOSDeleteDialog(
            title = "Delete this log?",
            onConfirm = {
                onDeleteOne(transaction)
                transactionToDelete = null
            },
            onDismiss = { transactionToDelete = null }
        )
    }

    // Delete All Dialog
    if (showDeleteAllConfirm) {
        IOSDeleteDialog(
            title = "Delete all logs?",
            onConfirm = {
                onDeleteAll()
                showDeleteAllConfirm = false
            },
            onDismiss = { showDeleteAllConfirm = false }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(iOSBlue)) {
        // --- HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 24.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Export Button (Left)
            if (transactions.isNotEmpty()) {
                IconButton(
                    onClick = { exportTransactionsToCSV(context, transactions) },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Default.Share, "Export CSV", tint = Color.White)
                }
            }

            Text(
                "Travel Logs", // Shorter text to fit between icons
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            // Delete All Button (Right)
            if (transactions.isNotEmpty()) {
                IconButton(
                    onClick = { showDeleteAllConfirm = true },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Default.Delete, "Delete All", tint = Color.White)
                }
            }
        }

        // --- CONTENT ---
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF2F2F7),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (transactions.isEmpty()) {
                    Text(
                        "No logs found",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 100.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(transactions) { transaction ->
                            TransactionListItem(
                                transaction = transaction,
                                onDelete = { transactionToDelete = transaction }
                            )
                        }
                    }
                }

                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = iOSBlue,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 24.dp, bottom = 48.dp)
                ) {
                    Icon(Icons.Default.Add, "Add", modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

@Composable
fun TransactionListItem(transaction: TransactionEntity, onDelete: () -> Unit) {
    val dateStr = SimpleDateFormat("EEEE, h:mm a", Locale.getDefault()).format(Date(transaction.date))
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("₹${transaction.amount}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(transaction.note.ifEmpty { "No remarks" }, color = Color.Gray, fontSize = 14.sp)
                Text(
                    "With: ${transaction.participants.joinToString(", ")}",
                    fontSize = 12.sp,
                    color = iOSBlue,
                    fontWeight = FontWeight.Medium
                )
                Text(dateStr, color = Color.LightGray, fontSize = 11.sp)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete", tint = Color.Red.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun IOSDeleteDialog(
    title: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(
                    color = Color.White.copy(alpha = 0.98f),
                    shape = RoundedCornerShape(14.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 17.sp),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "This action cannot be undone.",
                    style = TextStyle(fontSize = 13.sp),
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }

            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

            Row(modifier = Modifier.height(44.dp).fillMaxWidth()) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                ) {
                    Text("Cancel", color = iOSBlue, fontSize = 17.sp)
                }

                Box(modifier = Modifier.width(0.5.dp).fillMaxHeight().background(Color.LightGray))

                TextButton(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                ) {
                    Text("Delete", color = Color.Red, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- Put this at the very bottom of TransactionListScreen.kt ---

fun exportTransactionsToCSV(context: Context, transactions: List<TransactionEntity>) {
    // 1. Header with your specific order
    val csvHeader = "Date,Amount,Participants,Remarks\n"

    // 2. Mapping data to match the header order
    val csvData = transactions.joinToString("\n") {
        val date = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(it.date))
        // Using | to separate names so they don't break the CSV columns
        val participants = it.participants.joinToString("|")
        // Format: Date, Amount, Participants, Remarks
        "$date,${it.amount},$participants,${it.note}"
    }

    try {
        val filename = "Travel_Allowance_Logs.csv"
        val file = File(context.cacheDir, filename)
        file.writeText(csvHeader + csvData)

        // Generate secure URI using the FileProvider defined in Manifest
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        // Share Intent
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Export Logs"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}