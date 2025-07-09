package com.giovanna.amatucci.melisearch.domain.model

data class ProductDetail(
    val id: String,
    val title: String,
    val imageUrls: List<String>,
    val attributes: List<ProductAttribute>,
    val description: String
)
