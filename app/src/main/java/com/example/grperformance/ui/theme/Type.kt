package com.example.grperformance.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Note: iOSTextBlack and iOSTextGray should be defined in your Color.kt
// If not yet defined, you can replace them with Color.Black and Color.Gray

val Typography = Typography(
    // Used for the Main Screen "Transactions" title
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        letterSpacing = (-0.5).sp,
        color = iOSTextBlack
    ),
    // Used for section headers like "INVOLVED PEOPLE"
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 0.sp,
        color = iOSTextBlack
    ),
    // Used for secondary text or descriptions
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.25.sp,
        color = iOSTextGray
    ),
    // Used for smaller labels and "Select All" buttons
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        letterSpacing = 0.5.sp,
        color = iOSTextGray
    )
)