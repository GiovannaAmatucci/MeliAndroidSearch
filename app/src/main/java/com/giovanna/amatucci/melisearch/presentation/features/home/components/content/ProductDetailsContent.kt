package com.giovanna.amatucci.melisearch.presentation.features.home.components.content

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.giovanna.amatucci.melisearch.R
import com.giovanna.amatucci.melisearch.domain.model.ProductDetail
import com.giovanna.amatucci.melisearch.presentation.features.home.components.textstate.ProductAttributeItem
import com.giovanna.amatucci.melisearch.presentation.features.home.components.textstate.ProductInfoSection

@Composable
fun ProductDetailsContent(
    product: ProductDetail?, modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            ProductImages(images = product?.imageUrls ?: emptyList())
            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
        item {
            ProductInfoSection(
                title = product?.title.orEmpty(), description = product?.description.orEmpty()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        product?.attributes?.takeIf { it.isNotEmpty() }?.let { attributes ->
            item {
                Text(
                    text = stringResource(R.string.product_detail_section_title_attributes),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(attributes) { attribute ->
                ProductAttributeItem(attribute = attribute)
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}