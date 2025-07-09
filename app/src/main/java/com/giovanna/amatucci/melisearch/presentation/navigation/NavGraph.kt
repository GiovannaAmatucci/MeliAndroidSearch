package com.giovanna.amatucci.melisearch.presentation.navigation

import com.giovanna.amatucci.melisearch.domain.model.ProductDetail
import kotlinx.serialization.Serializable

@Serializable
object LoginScreen

@Serializable
object HomeScreen

@Serializable
data class ProductDetailScreen(val productId: String)