package com.giovanna.amatucci.melisearch.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailResponse(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("price") val price: Double,
    @SerialName("pictures") val pictures: List<Picture>,
    @SerialName("soldQuantity") val soldQuantity: Int,
    @SerialName("availableQuantity") val availableQuantity: Int,
    @SerialName("warranty") val warranty: String?
)

@Serializable
data class Picture(
    @SerialName("url") val url: String
)