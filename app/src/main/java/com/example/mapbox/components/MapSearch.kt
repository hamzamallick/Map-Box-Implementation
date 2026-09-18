package com.example.mapbox.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.mapbox.search.SearchSuggestionsCallback
import com.mapbox.search.SearchSelectionCallback
import com.mapbox.search.result.SearchSuggestion
import com.mapbox.search.result.SearchResult
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mapbox.search.ApiType
import com.mapbox.search.SearchEngine
import com.mapbox.search.SearchEngineSettings
import com.mapbox.search.ResponseInfo
import com.mapbox.search.SearchOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.graphics.Color
import com.mapbox.geojson.Point

@Composable
fun MapSearch(
    onLocationSelected: (Point, String) -> Unit,
    modifier: Modifier = Modifier
) {



    val searchEngine = remember {

        SearchEngine.createSearchEngine(
            apiType = ApiType.GEOCODING,
            settings = SearchEngineSettings()
        )
    }

    var query by remember {
        mutableStateOf("")
    }


    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = {
            Text("Search places...")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {

                if (query.isNotBlank()) {

                    val searchOptions = SearchOptions(
                        limit = 5
                    )

                    searchEngine.search(
                        query,
                        searchOptions,
                        object : SearchSuggestionsCallback {

                            override fun onSuggestions(
                                suggestions: List<SearchSuggestion>,
                                responseInfo: ResponseInfo
                            ) {

                                val suggestion = suggestions.firstOrNull()

                                if (suggestion != null) {

                                    searchEngine.select(
                                        suggestion,
                                        object : SearchSelectionCallback {

                                            override fun onResult(
                                                suggestion: SearchSuggestion,
                                                result: SearchResult,
                                                responseInfo: ResponseInfo
                                            ) {

                                                val coordinate = result.coordinate

                                                onLocationSelected(
                                                    coordinate,
                                                    result.name ?: "Unknown location"
                                                )
                                            }

                                            override fun onSuggestions(
                                                suggestions: List<SearchSuggestion>,
                                                responseInfo: ResponseInfo
                                            ) {
                                            }

                                            override fun onResults(
                                                suggestion: SearchSuggestion,
                                                results: List<SearchResult>,
                                                responseInfo: ResponseInfo
                                            ) {
                                            }

                                            override fun onError(
                                                e: Exception
                                            ) {
                                            }
                                        }
                                    )
                                }
                            }

                            override fun onError(
                                e: Exception
                            ) {
                            }
                        }
                    )
                }
            }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Gray,
            unfocusedContainerColor = Color.LightGray,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedPlaceholderColor = Color.LightGray,
            unfocusedPlaceholderColor = Color.LightGray
        )
    )
}