package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TemperatureUnit
import com.example.data.model.District
import com.example.data.model.DistrictSummary
import com.example.ui.components.WeatherIconView
import com.example.ui.theme.RainCyan
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.viewmodel.DistrictFilter
import com.example.ui.viewmodel.DistrictSort
import com.example.utils.UnitConverter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistrictsScreen(
    districtsList: List<DistrictSummary>,
    isLoading: Boolean,
    searchQuery: String,
    selectedFilter: DistrictFilter,
    sortOption: DistrictSort,
    tempUnit: TemperatureUnit,
    onSearchQueryChange: (String) -> Unit,
    onFilterChange: (DistrictFilter) -> Unit,
    onSortChange: (DistrictSort) -> Unit,
    onDistrictClick: (District) -> Unit,
    onRefreshAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showSortMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("districts_screen")
    ) {
        // Top App Bar
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "All 38 Districts • सभी ३८ ज़िले",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Real-time weather across Bihar",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            actions = {
                // Sort Menu Icon
                Box {
                    IconButton(
                        onClick = { showSortMenu = true },
                        modifier = Modifier.testTag("sort_districts_button")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Sort,
                            contentDescription = "Sort districts"
                        )
                    }

                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Name: A to Z") },
                            leadingIcon = {
                                if (sortOption == DistrictSort.NAME_ASC) Icon(Icons.Filled.Check, null)
                            },
                            onClick = {
                                onSortChange(DistrictSort.NAME_ASC)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Temperature: High to Low") },
                            leadingIcon = {
                                if (sortOption == DistrictSort.TEMP_DESC) Icon(Icons.Filled.Check, null)
                            },
                            onClick = {
                                onSortChange(DistrictSort.TEMP_DESC)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Temperature: Low to High") },
                            leadingIcon = {
                                if (sortOption == DistrictSort.TEMP_ASC) Icon(Icons.Filled.Check, null)
                            },
                            onClick = {
                                onSortChange(DistrictSort.TEMP_ASC)
                                showSortMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Rain Probability") },
                            leadingIcon = {
                                if (sortOption == DistrictSort.RAIN_DESC) Icon(Icons.Filled.Check, null)
                            },
                            onClick = {
                                onSortChange(DistrictSort.RAIN_DESC)
                                showSortMenu = false
                            }
                        )
                    }
                }

                // Refresh Button
                IconButton(
                    onClick = onRefreshAll,
                    enabled = !isLoading,
                    modifier = Modifier.testTag("refresh_all_districts_button")
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = SkyBluePrimary
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Refresh all districts"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        // Search Text Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .testTag("districts_search_field"),
            placeholder = { Text("Search by district name (e.g. Gaya, पटना, Saran)...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = SkyBluePrimary
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Filled.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )

        // Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == DistrictFilter.ALL,
                onClick = { onFilterChange(DistrictFilter.ALL) },
                label = { Text("All (38)") },
                modifier = Modifier.testTag("filter_chip_all")
            )
            FilterChip(
                selected = selectedFilter == DistrictFilter.RAINY,
                onClick = { onFilterChange(DistrictFilter.RAINY) },
                label = { Text("Rainy • बारिश") },
                modifier = Modifier.testTag("filter_chip_rainy")
            )
            FilterChip(
                selected = selectedFilter == DistrictFilter.CLEAR,
                onClick = { onFilterChange(DistrictFilter.CLEAR) },
                label = { Text("Clear • साफ़") },
                modifier = Modifier.testTag("filter_chip_clear")
            )
            FilterChip(
                selected = selectedFilter == DistrictFilter.CLOUDY,
                onClick = { onFilterChange(DistrictFilter.CLOUDY) },
                label = { Text("Cloudy • बादल") },
                modifier = Modifier.testTag("filter_chip_cloudy")
            )
            FilterChip(
                selected = selectedFilter == DistrictFilter.HOT,
                onClick = { onFilterChange(DistrictFilter.HOT) },
                label = { Text("Hot (>35°C)") },
                modifier = Modifier.testTag("filter_chip_hot")
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Districts List
        if (districtsList.isEmpty() && !isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.SearchOff,
                        contentDescription = "No districts found",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No districts match \"$searchQuery\"",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("districts_list"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(districtsList, key = { it.district.id }) { item ->
                    DistrictListItem(
                        summary = item,
                        tempUnit = tempUnit,
                        onClick = { onDistrictClick(item.district) }
                    )
                }
            }
        }
    }
}

@Composable
fun DistrictListItem(
    summary: DistrictSummary,
    tempUnit: TemperatureUnit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("district_card_${summary.district.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Column: Name & Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = summary.district.nameEn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "(${summary.district.nameHi})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${summary.district.division} Div • ${summary.district.zone}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${summary.condition.labelEn} • ${summary.condition.labelHi}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SkyBluePrimary,
                        fontWeight = FontWeight.Medium
                    )

                    if (summary.rainProbability > 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.WaterDrop,
                                contentDescription = "Rain",
                                tint = RainCyan,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${summary.rainProbability}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = RainCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Right Column: Weather Icon & Current Temp & Min/Max
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    WeatherIconView(
                        iconType = summary.condition.iconType,
                        size = 36.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = UnitConverter.formatTemperature(summary.tempC, tempUnit),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "L: ${UnitConverter.formatTemperature(summary.minTempC, tempUnit)} • H: ${UnitConverter.formatTemperature(summary.maxTempC, tempUnit)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }
        }
    }
}
