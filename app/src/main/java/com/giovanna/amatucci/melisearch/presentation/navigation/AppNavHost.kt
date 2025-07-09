package com.giovanna.amatucci.melisearch.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.giovanna.amatucci.melisearch.presentation.features.home.screens.HomeScreen
import com.giovanna.amatucci.melisearch.presentation.features.home.screens.ProductDetailsScreen
import com.giovanna.amatucci.melisearch.presentation.features.login.LoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = LoginScreen,
        modifier = Modifier
    ) {
        composable<LoginScreen> {
            LoginScreen(
                navigateToHome = {
                    navController.navigate(HomeScreen) {
                        popUpTo(LoginScreen) { inclusive = true }
                    }
                }
            )
        }
        composable<HomeScreen> {
            HomeScreen(navigateToDetail = {
                navController.navigate(ProductDetailScreen(productId = it))
            })
        }
        composable<ProductDetailScreen> {
            ProductDetailsScreen(
                onBackClick = { navController.popBackStack() },
                id = it.arguments?.getString(ProductDetailScreen::productId.name).orEmpty()
            )
        }
    }
}


