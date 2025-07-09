package com.giovanna.amatucci.melisearch.domain.model

data class ProductShorthand(
    val id: String,
    val title: String,
    val imageUrls: List<String>,
    val displayPrice: String,
    val description: String?,
    val thumbnailUrl: String
)