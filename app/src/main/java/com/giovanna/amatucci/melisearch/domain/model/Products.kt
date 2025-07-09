package com.giovanna.amatucci.melisearch.domain.model

data class Products(
    val id: String,
    val name: String,
    val imageUrls: List<String>,
    val brand: String?,
    val model: String?,
    val color: String?,
    val internalMemory: String?,
    val ram: String?,
    val releaseYear: String?,
    val screenSize: String?,
    val screenResolution: String?,
    val mainRearCamera: String?,
    val mainFrontCamera: String?,
    val description: String?
)

data class Pagination(
    val totalItems: Int,
    val itemsPerPage: Int,
    val offset: Int
)

data class ProductPage(
    val keywords: String,
    val products: List<Products>,
    val pagination: Pagination
)