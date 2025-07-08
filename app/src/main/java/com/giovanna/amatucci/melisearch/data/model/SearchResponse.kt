package com.giovanna.amatucci.melisearch.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val keywords: String,
    val paging: Paging,
    val results: List<SearchResultItem>,
    @SerialName("used_attributes")
    val usedAttributes: List<Attribute>,
    @SerialName("query_type")
    val queryType: String
)

@Serializable
data class Paging(
    val total: Int,
    val limit: Int,
    val offset: Int
)

@Serializable
data class SearchResultItem(
    val id: String,
    @SerialName("date_created")
    val dateCreated: String? = null,
    @SerialName("catalog_product_id")
    val catalogProductId: String? = null,
    @SerialName("pdp_types")
    val pdpTypes: List<String>? = emptyList(),
    val status: String? = null,
    @SerialName("domain_id")
    val domainId: String? = null,
    val settings: Settings? = null,
    val name: String? = null,
    @SerialName("main_features")
    val mainFeatures: List<String>? = emptyList(),
    val attributes: List<Attribute>? = emptyList(),
    val pictures: List<Picture>? = emptyList(),
    @SerialName("parent_id")
    val parentId: String? = null,
    @SerialName("children_ids")
    val childrenIds: List<String>? = emptyList(),
    @SerialName("quality_type")
    val qualityType: String? = null,
    val priority: String? = null,
    val type: String? = null,
    @SerialName("site_id")
    val siteId: String? = null,
    val keywords: String? = null,
    val variations: List<String>? = emptyList(),
    val description: String? = null
)

@Serializable
data class Settings(
    @SerialName("listing_strategy")
    val listingStrategy: String,
    val exclusive: Boolean
)

@Serializable
data class Attribute(
    val id: String,
    val name: String,
    @SerialName("value_id")
    val valueId: String? = null,
    @SerialName("value_name")
    val valueName: String
)

@Serializable
data class Picture(
    val id: String,
    val url: String
)