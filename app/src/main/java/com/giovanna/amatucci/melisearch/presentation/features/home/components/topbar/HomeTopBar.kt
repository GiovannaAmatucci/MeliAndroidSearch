package com.giovanna.amatucci.melisearch.presentation.features.home.components.topbar

import HomeSearchTopBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.giovanna.amatucci.melisearch.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
 fun HomeTopBar(
    searchQuery: String,
    searchHistory: List<String>,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onHistoryItemClicked: (String) -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.primaryContainer) {
        HomeSearchTopBar(
            text = searchQuery,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            searchResults = searchHistory,
            onResultClick = onHistoryItemClicked,
            placeholder = R.string.search_bar_placeholder
        )
    }
}