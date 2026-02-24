package com.example.grperformance

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge // Add this import
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
        // 1. MUST BE CALLED BEFORE super.onCreate
        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

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
                @Suppress("UNCHECKED_CAST")
                return TransactionViewModel(db.transactionDao()) as T
            }
        })[TransactionViewModel::class.java]

        // 4. Set the Content
        setContent {
            GRPerformanceTheme {
                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window

                        // Force transparent bars
                        window.navigationBarColor = android.graphics.Color.TRANSPARENT
                        window.statusBarColor = android.graphics.Color.TRANSPARENT

                        val controller = WindowCompat.getInsetsController(window, view)

                        // Since your top bar is Blue (Dark), we want White icons on top
                        controller.isAppearanceLightStatusBars = false

                        // Since your bottom area is Grey (Light), we want Dark icons at the bottom
                        controller.isAppearanceLightNavigationBars = true
                    }
                }

                NavGraph(viewModel = viewModel)
            }
        }
    }
}