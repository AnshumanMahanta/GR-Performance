package com.example.grperformance.ui.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grperformance.data.local.TransactionEntity
import com.example.grperformance.ui.components.GroupMemberChip
import com.example.grperformance.ui.screens.list.iOSBlue
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddTransactionScreen(
    onBackClick: () -> Unit,
    onSaveClick: (TransactionEntity) -> Unit
) {
    // --- State Management ---
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    val calendar = remember { Calendar.getInstance() }
    var dateMillis by remember { mutableLongStateOf(calendar.timeInMillis) }

    val groupMembers = remember {
        listOf("Mentor", "Sai Subham Sahu", "Snehalata Pattnaik", "Sibam Dash", "Yugantik Mahapatra", "Anshuman Mahanta")
    }
    val selectedPeople = remember { mutableStateListOf<String>() }

    val isAllSelected = selectedPeople.size == groupMembers.size
    val selectAllText = if (isAllSelected) "Deselect All" else "Select All"

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // --- UI Structure ---
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // FIXED HEADER: Stays blue and fixed at the top
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(iOSBlue)
                    .statusBarsPadding()
                    .padding(top = 16.dp, bottom = 16.dp, start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                }
                Text(
                    "Add Transaction",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        // Changed to the background gray so the navigation area at the bottom is white/gray
        containerColor = Color(0xFFF2F2F7)
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .navigationBarsPadding(), // Ensures "Save" button stays above the gesture bar
            color = Color(0xFFF2F2F7)
        ) {
            // We use a Box to draw a small blue rectangle at the very top
            // This maintains the "rounded corner" look against the blue header
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(iOSBlue)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color(0xFFF2F2F7),
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                        )
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // DETAILS SECTION
                    LabelRow(
                        label = "DETAILS",
                        actionText = "Clear All",
                        onAction = { amount = ""; note = "" }
                    )

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            TextField(
                                value = amount,
                                onValueChange = { amount = it },
                                placeholder = { Text("Amount (₹)", color = Color.LightGray) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    cursorColor = iOSBlue
                                ),
                                textStyle = TextStyle(fontSize = 16.sp),
                                singleLine = true
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = Color.LightGray.copy(alpha = 0.5f)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Date/Time", color = Color.Gray, fontSize = 14.sp)
                                Surface(
                                    onClick = { showDatePicker = true },
                                    color = Color(0xFFF2F2F7),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val sdf = SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.getDefault())
                                        Text(sdf.format(Date(dateMillis)), fontSize = 13.sp)
                                        Spacer(Modifier.width(6.dp))
                                        Icon(Icons.Default.Refresh, null, modifier = Modifier.size(14.dp), tint = iOSBlue)
                                    }
                                }
                            }
                        }
                    }

                    // INVOLVED PEOPLE SECTION
                    LabelRow(
                        label = "INVOLVED PEOPLE",
                        actionText = selectAllText,
                        onAction = {
                            if (isAllSelected) {
                                selectedPeople.clear()
                            } else {
                                selectedPeople.clear()
                                selectedPeople.addAll(groupMembers)
                            }
                        }
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        groupMembers.forEach { person ->
                            GroupMemberChip(
                                name = person,
                                isSelected = selectedPeople.contains(person),
                                onSelectionChanged = { isSelected ->
                                    if (isSelected) selectedPeople.add(person) else selectedPeople.remove(person)
                                }
                            )
                        }
                    }

                    // REMARKS SECTION
                    Text("REMARKS", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        TextField(
                            value = note,
                            onValueChange = { note = it },
                            placeholder = { Text("Note / Reason / Remarks", color = Color.LightGray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            textStyle = TextStyle(fontSize = 16.sp)
                        )
                    }

                    // SAVE BUTTON
                    Button(
                        onClick = {
                            val amountDouble = amount.toDoubleOrNull()
                            if (amountDouble != null && selectedPeople.isNotEmpty()) {
                                onSaveClick(
                                    TransactionEntity(
                                        amount = amountDouble,
                                        date = dateMillis,
                                        note = note,
                                        participants = selectedPeople.toList()
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = iOSBlue)
                    ) {
                        Text("Save Transaction", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }

    // --- DIALOGS ---
    if (showDatePicker) {
        val dateState = rememberDatePickerState(initialSelectedDateMillis = dateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selected = dateState.selectedDateMillis ?: dateMillis
                    calendar.timeInMillis = selected
                    showDatePicker = false
                    showTimePicker = true
                }) { Text("Next", color = iOSBlue) }
            }
        ) { DatePicker(state = dateState) }
    }

    if (showTimePicker) {
        val timeState = rememberTimePickerState(
            initialHour = calendar.get(Calendar.HOUR_OF_DAY),
            initialMinute = calendar.get(Calendar.MINUTE)
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    calendar.set(Calendar.HOUR_OF_DAY, timeState.hour)
                    calendar.set(Calendar.MINUTE, timeState.minute)
                    dateMillis = calendar.timeInMillis
                    showTimePicker = false
                }) { Text("OK", color = iOSBlue) }
            },
            text = {
                Box(modifier = Modifier.padding(top = 16.dp)) {
                    TimePicker(state = timeState)
                }
            }
        )
    }
}

@Composable
fun LabelRow(label: String, actionText: String, onAction: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        TextButton(
            onClick = onAction,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(actionText, color = iOSBlue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}