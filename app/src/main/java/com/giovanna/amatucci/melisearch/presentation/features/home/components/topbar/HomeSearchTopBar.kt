import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchTopBar(
    text: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    onResultClick: (String) -> Unit,
    @StringRes placeholder: Int,
    supportingContent: (@Composable (String) -> Unit)? = null,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val modifier = if (expanded) Modifier.fillMaxWidth()
    else Modifier.fillMaxWidth().padding(all = 12.dp)

    SearchBar(
        modifier = modifier,
        inputField = {
            SearchBarDefaults.InputField(
                modifier = Modifier.background(color = Color.Transparent),
                query = text,
                onQueryChange = onQueryChange,
                onSearch = {
                    onSearch(text)
                    expanded = false
                },
                expanded = expanded,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                onExpandedChange = { expanded = it },
                placeholder = {
                    Text(
                        stringResource(placeholder),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                },
                leadingIcon = {
                    if (expanded) {
                        IconButton(onClick = { expanded = false }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar"
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Search, contentDescription = "Pesquisar"
                        )
                    }
                },
                trailingIcon = {
                    if (expanded) {
                        IconButton(
                            onClick = {
                                if (text.isNotEmpty()) {
                                    onQueryChange("")
                                } else {
                                    expanded = false
                                }
                            }

                        ) {
                            Icon(
                                Icons.Default.Close, contentDescription = "Fechar e limpar busca"
                            )
                        }
                    }
                }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },

    ) {
        LazyColumn {
            items(count = searchResults.size) { index ->
                val resultText = searchResults[index]
                ListItem(
                    headlineContent = { Text(resultText, style = MaterialTheme.typography.bodySmall) },
                    supportingContent = supportingContent?.let { { it(resultText) } },
                    leadingContent = {
                        Icon(
                            Icons.Default.AccessTime, contentDescription = "Access item"
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    modifier = modifier.clickable {
                        onResultClick(resultText)
                        expanded = false
                    })
            }
        }
    }
}
