package com.example.grperformance.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grperformance.ui.theme.iOSBlue

@Composable
fun GroupMemberChip(
    name: String,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit
) {
    // Smoothly transition the background color like iOS
    val backgroundColor by animateColorAsState(
        if (isSelected) iOSBlue else Color.Transparent, label = ""
    )
    val textColor by animateColorAsState(
        if (isSelected) Color.White else Color.Black, label = ""
    )
    val borderColor = if (isSelected) iOSBlue else Color.LightGray.copy(alpha = 0.5f)

    Box(
        modifier = Modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable { onSelectionChanged(!isSelected) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            color = textColor,
            fontSize = 14.sp
        )
    }
}