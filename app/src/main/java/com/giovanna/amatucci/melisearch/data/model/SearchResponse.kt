package com.giovanna.amatucci.melisearch.data.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("results") val results: List<Product>
)

@Serializable
data class Product(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("price") val price: Double,
    @SerialName("thumbnail")val thumbnail: String
)