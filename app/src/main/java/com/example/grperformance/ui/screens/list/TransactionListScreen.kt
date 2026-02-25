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
val ScreenBackground = Color(0xFFF2F2F7) // Define it once for consistency

@Composable
fun TransactionListScreen(
    transactions: List<TransactionEntity>,
    onAddClick: () -> Unit,
    onDeleteOne: (TransactionEntity) -> Unit,
    onDeleteAll: () -> Unit
) {
    val context = LocalContext.current
    var transactionToDelete by remember { mutableStateOf<TransactionEntity?>(null) }
    var showDeleteAllConfirm by remember { mutableStateOf(false) }

    // --- DIALOGS (UNCHANGED) ---
    transactionToDelete?.let { transaction ->
        IOSDeleteDialog(
            title = "Delete this log?",
            onConfirm = { onDeleteOne(transaction); transactionToDelete = null },
            onDismiss = { transactionToDelete = null }
        )
    }

    if (showDeleteAllConfirm) {
        IOSDeleteDialog(
            title = "Delete all logs?",
            onConfirm = { onDeleteAll(); showDeleteAllConfirm = false },
            onDismiss = { showDeleteAllConfirm = false }
        )
    }

    // --- UPDATED OUTER COLUMN ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(iOSBlue)
            // This prevents the Blue background from leaking into the bottom nav area
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
    ) {
        // --- HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 24.dp, bottom = 24.dp, start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (transactions.isNotEmpty()) {
                IconButton(
                    onClick = { exportTransactionsToCSV(context, transactions) },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) { Icon(Icons.Default.Share, "Export CSV", tint = Color.White) }
            }

            Text("Travel Logs", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            if (transactions.isNotEmpty()) {
                IconButton(
                    onClick = { showDeleteAllConfirm = true },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) { Icon(Icons.Default.Delete, "Delete All", tint = Color.White) }
            }
        }

        // --- CONTENT AREA ---
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = ScreenBackground, // This grey color now fills the bottom nav area
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (transactions.isEmpty()) {
                    Text("No logs found", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 180.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(transactions) { transaction ->
                            TransactionListItem(transaction = transaction, onDelete = { transactionToDelete = transaction })
                        }
                    }
                }

                // --- SOLID FIXED FOOTER ---
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(ScreenBackground)
                        .navigationBarsPadding() // Pushes text above the buttons
                        .padding(vertical = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Made with 💖 by Glausco Technologies",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                FloatingActionButton(
                    onClick = onAddClick,
                    containerColor = iOSBlue,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .navigationBarsPadding() // Pushes FAB above the buttons
                        .padding(end = 24.dp, bottom = 85.dp)
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
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("₹${transaction.amount}", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.Black)
                Text(transaction.note.ifEmpty { "No remarks" }, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("With: ${transaction.participants.joinToString(", ")}", fontSize = 13.sp, color = iOSBlue, fontWeight = FontWeight.SemiBold)
                Text(dateStr, color = Color.LightGray, fontSize = 11.sp)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete", tint = Color.Red.copy(alpha = 0.4f), modifier = Modifier.size(20.dp))
            }
        }
    }
}

// --- REST OF THE CODE (IOSDeleteDialog & exportTransactionsToCSV) ---
// (Paste your existing IOSDeleteDialog and export function here)

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

fun exportTransactionsToCSV(context: Context, transactions: List<TransactionEntity>) {
    val csvHeader = "Date,Amount,Participants,Remarks\n"
    val csvData = transactions.joinToString("\n") {
        val date = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(Date(it.date))
        val participants = it.participants.joinToString("|")
        "$date,${it.amount},$participants,${it.note}"
    }

    try {
        val filename = "Travel_Allowance_Logs.csv"
        val file = File(context.cacheDir, filename)
        file.writeText(csvHeader + csvData)

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

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