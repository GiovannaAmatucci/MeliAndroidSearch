package com.giovanna.amatucci.melisearch.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailResponse(
    @SerialName("id") val id: String?,
    @SerialName("catalog_product_id") val catalogProductId: String?,
    @SerialName("status") val status: String?,
    @SerialName("pdp_types") val pdpTypes: List<String>? = null,
    @SerialName("domain_id") val domainId: String?,
    @SerialName("permalink") val permalink: String?,
    @SerialName("name") val name: String?,
    @SerialName("family_name") val familyName: String?,
    @SerialName("type") val type: String?,
    @SerialName("pickers") val pickers: List<Picker>? = null,
    @SerialName("pictures") val pictures: List<PictureDetail>? = null,
    @SerialName("description_pictures") val descriptionPictures: List<String>? = null,
    @SerialName("main_features") val mainFeatures: List<MainFeature>? = null,
    @SerialName("disclaimers") val disclaimers: List<String>? = null,
    @SerialName("attributes") val attributes: List<AttributeDetail>? = null,
    @SerialName("short_description") val shortDescription: ShortDescription? = null,
    @SerialName("parent_id") val parentId: String?,
    @SerialName("children_ids") val childrenIds: List<String>? = null,
    @SerialName("settings") val settings: SettingsData? = null,
    @SerialName("quality_type") val qualityType: String?,
    @SerialName("tags") val tags: List<String>? = null,
    @SerialName("date_created") val dateCreated: String?,
    @SerialName("last_updated") val lastUpdated: String?,
    @SerialName("grouper_id") val grouperId: String? = null,
    @SerialName("experiments") val experiments: Map<String, String>? = null
)

@Serializable
data class Picker(
    @SerialName("picker_id") val pickerId: String?,
    @SerialName("picker_name") val pickerName: String?,
    @SerialName("products") val products: List<PickerProduct>? = null,
    @SerialName("tags") val tags: List<String>? = null,
    @SerialName("attributes") val pickerAttributes: List<PickerAttribute>? = null,
    @SerialName("value_name_delimiter") val valueNameDelimiter: String?
)

@Serializable
data class PickerProduct(
    @SerialName("product_id") val productId: String?,
    @SerialName("picker_label") val pickerLabel: String?,
    @SerialName("picture_id") val pictureId: String?,
    @SerialName("thumbnail") val thumbnail: String?,
    @SerialName("tags") val tags: List<String>? = null,
    @SerialName("permalink") val permalink: String?,
    @SerialName("product_name") val productName: String?,
    @SerialName("auto_completed") val autoCompleted: Boolean?
)

@Serializable
data class PickerAttribute(
    @SerialName("attribute_id") val attributeId: String?,
    @SerialName("template") val template: String?
)

@Serializable
data class PictureDetail(
    @SerialName("id") val id: String?,
    @SerialName("url") val url: String?,
    @SerialName("suggested_for_picker") val suggestedForPicker: List<String>? = null,
    @SerialName("max_width") val maxWidth: Int?,
    @SerialName("max_height") val maxHeight: Int?,
    @SerialName("tags") val tags: List<String>? = null
)

@Serializable
data class MainFeature(
    @SerialName("text") val text: String?,
    @SerialName("type") val type: String?,
    @SerialName("metadata") val metadata: Map<String, String>? = null
)

@Serializable
data class AttributeDetail(
    @SerialName("id") val id: String?,
    @SerialName("name") val name: String?,
    @SerialName("value_id") val valueId: String? = null,
    @SerialName("value_name") val valueName: String?,
    @SerialName("values") val values: List<AttributeValue>? = null
)

@Serializable
data class AttributeValue(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String?
)

@Serializable
data class ShortDescription(
    @SerialName("type") val type: String?,
    @SerialName("content") val content: String?
)

@Serializable
data class SettingsData(
    @SerialName("content") val content: String?,
    @SerialName("listing_strategy") val listingStrategy: String?,
    @SerialName("with_enhanced_pictures") val withEnhancedPictures: Boolean?,
    @SerialName("base_site_product_id") val baseSiteProductId: String? = null,
    @SerialName("exclusive") val exclusive: Boolean?
)