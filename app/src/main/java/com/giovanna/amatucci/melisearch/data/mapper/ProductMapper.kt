package com.giovanna.amatucci.melisearch.data.mapper

import com.giovanna.amatucci.melisearch.data.model.AttributeDetail
import com.giovanna.amatucci.melisearch.data.model.ProductDetailResponse
import com.giovanna.amatucci.melisearch.data.model.SearchResultItem
import com.giovanna.amatucci.melisearch.domain.model.ProductAttribute
import com.giovanna.amatucci.melisearch.domain.model.ProductDetail
import com.giovanna.amatucci.melisearch.domain.model.Products

fun SearchResultItem.toDomain(): Products {
    fun findAttribute(id: String): String? = attributes?.firstOrNull { it.id == id }?.valueName

    return Products(
        id = this.id,
        name = this.name.orEmpty(),
        imageUrls = this.pictures?.map { it.url } ?: emptyList(),
        brand = findAttribute("BRAND"),
        model = findAttribute("MODEL"),
        color = findAttribute("COLOR"),
        internalMemory = findAttribute("INTERNAL_MEMORY"),
        ram = findAttribute("RAM"),
        releaseYear = findAttribute("RELEASE_YEAR"),
        screenSize = findAttribute("DISPLAY_SIZE"),
        screenResolution = findAttribute("DISPLAY_RESOLUTION"),
        mainRearCamera = findAttribute("MAIN_REAR_CAMERA_RESOLUTION"),
        mainFrontCamera = findAttribute("MAIN_FRONT_CAMERA_RESOLUTION"),
        description = this.description
    )
}

fun AttributeDetail.toDomain(): ProductAttribute? {
    return ProductAttribute(
        name = this.name.orEmpty(), value = this.valueName.orEmpty()
    )
}

fun ProductDetailResponse.toDomain(): ProductDetail {
    return ProductDetail(
        id = this.id.orEmpty(),
        title = this.name.orEmpty(),
        description = this.shortDescription?.content.orEmpty(),
        imageUrls = this.pictures?.mapNotNull { it.url?.replace("http://", "https://") } ?: emptyList(),
        attributes = this.attributes?.mapNotNull { it.toDomain() } ?: emptyList(),
    )
}

