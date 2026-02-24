package com.example.grperformance

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.grperformance.data.local.AppDatabase
import com.example.grperformance.navigation.NavGraph
import com.example.grperformance.ui.theme.GRPerformanceTheme
import com.example.grperformance.ui.viewmodel.TransactionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Enable Edge-to-Edge and 120Hz Support
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Request High Refresh Rate (120Hz)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.attributes.preferredDisplayModeId = 0
        }

        // 2. Initialize the Room Database
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "transaction-db"
        ).build()

        // 3. Create the ViewModel using a Factory
        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TransactionViewModel(db.transactionDao()) as T
            }
        })[TransactionViewModel::class.java]

        // 4. Set the Content
        setContent {
            GRPerformanceTheme {
                // UI Tweak: Make the Navigation Bar icons dark and the bar transparent
                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window
                        // Set the navigation bar color to transparent so our screen background shows through
                        window.navigationBarColor = android.graphics.Color.TRANSPARENT

                        val controller = WindowCompat.getInsetsController(window, view)
                        // Make status bar icons white (for blue header)
                        controller.isAppearanceLightStatusBars = false
                        // Make navigation bar icons dark (for white content area)
                        controller.isAppearanceLightNavigationBars = true
                    }
                }

                NavGraph(viewModel = viewModel)
            }
        }
    }
}