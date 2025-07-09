package com.giovanna.amatucci.melisearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.giovanna.amatucci.melisearch.presentation.navigation.AppNavHost
import com.giovanna.amatucci.melisearch.ui.theme.MeliTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.Transparent.toArgb(), Color.Transparent.toArgb()
                    ),
                    navigationBarStyle = SystemBarStyle.light(
                        Color.Transparent.toArgb(), Color.Transparent.toArgb()
                    )
                )
            MeliTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}

