package com.giovanna.amatucci.melisearch.presentation.features.home.components.content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.giovanna.amatucci.melisearch.presentation.features.home.components.image.NoImagePlaceholder

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
 fun ProductImages(images: List<String>) {
    if (images.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { images.size })

        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                GlideImage(
                    model = images[it],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
            }

            if (pagerState.pageCount > 1) {
                Spacer(modifier = Modifier.height(12.dp))
                PagerIndicator(
                    pageCount = pagerState.pageCount,
                    currentPage = pagerState.currentPage
                )
            }
        }
    } else {
        NoImagePlaceholder(modifier = Modifier.fillMaxWidth().height(300.dp))
    }
}