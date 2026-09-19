package com.example.mapbox.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.NorthWest
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.HorizontalDivider
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

    var searchSuggestions by remember {
        mutableStateOf<List<SearchSuggestion>>(emptyList())
    }

    Column(modifier = modifier) {

        OutlinedTextField(
            value = query,

            onValueChange = {
                query = it

                if (it.isNotBlank()) {

                    searchEngine.search(
                        it,
                        SearchOptions(limit = 5),
                        object : SearchSuggestionsCallback {

                            override fun onSuggestions(
                                suggestions: List<SearchSuggestion>,
                                responseInfo: ResponseInfo
                            ) {
                                searchSuggestions = suggestions
                            }

                            override fun onError(
                                e: Exception
                            ) {
                                searchSuggestions = emptyList()
                            }
                        }
                    )

                } else {
                    searchSuggestions = emptyList()
                }
            },

            modifier = Modifier
                .fillMaxWidth(),

            placeholder = {
                Text(
                    text = "Search here",
                    color = Color.Gray
                )
            },

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.DarkGray
                )
            },

            singleLine = true,

            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),

            keyboardActions = KeyboardActions(
                onSearch = {
                    // Don't automatically select first result
                }
            ),

            shape = RoundedCornerShape(30.dp),

            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,

                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,

                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,

                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,

                focusedLeadingIconColor = Color.DarkGray,
                unfocusedLeadingIconColor = Color.DarkGray
            )
        )

        searchSuggestions.forEachIndexed { index, suggestion ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clickable {

                        searchEngine.select(
                            suggestion,
                            object : SearchSelectionCallback {

                                override fun onResult(
                                    suggestion: SearchSuggestion,
                                    result: SearchResult,
                                    responseInfo: ResponseInfo
                                ) {

                                    val coordinate = result.coordinate

                                    val name =
                                        result.name ?: suggestion.name

                                    onLocationSelected(
                                        coordinate,
                                        name
                                    )

                                    query = name

                                    searchSuggestions =
                                        emptyList()
                                }

                                override fun onSuggestions(
                                    suggestions: List<SearchSuggestion>,
                                    responseInfo: ResponseInfo
                                ) {}

                                override fun onResults(
                                    suggestion: SearchSuggestion,
                                    results: List<SearchResult>,
                                    responseInfo: ResponseInfo
                                ) {}

                                override fun onError(
                                    e: Exception
                                ) {
                                    searchSuggestions = emptyList()
                                }
                            }
                        )
                    }
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {


                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(8.dp)
                    )


                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    ) {

                        Text(
                            text = suggestion.name,
                            color = Color.DarkGray,
                            fontSize = 16.sp
                        )

                        Text(
                            text = "Search result",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }


                    Icon(
                        imageVector = Icons.Default.NorthWest,
                        contentDescription = "Select",
                        tint = Color.DarkGray,
                        modifier = Modifier
                            .size(28.dp)
                            .padding(4.dp)
                    )
                }
            }

            if (index < searchSuggestions.lastIndex) {

                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = 1.dp
                )
            }
        }


    }



}