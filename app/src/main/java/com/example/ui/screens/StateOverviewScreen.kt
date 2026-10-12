package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TemperatureUnit
import com.example.data.model.District
import com.example.data.model.DistrictSummary
import com.example.data.model.StateWeatherSummary
import com.example.ui.components.WeatherIconView
import com.example.ui.theme.*
import com.example.utils.UnitConverter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StateOverviewScreen(
    stateSummary: StateWeatherSummary?,
    allDistricts: List<DistrictSummary>,
    isLoading: Boolean,
    tempUnit: TemperatureUnit,
    onDistrictClick: (District) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("state_overview_screen")
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Bihar State Overview • बिहार सारांश",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Statewide meteorological distribution",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = onRefresh,
                    enabled = !isLoading,
                    modifier = Modifier.testTag("refresh_overview_button")
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
                            contentDescription = "Refresh"
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        if (stateSummary == null || allDistricts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SkyBluePrimary)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("state_overview_content"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Calculated State Average Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("state_avg_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = SkyBluePrimary
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(18.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Calculated State Average • राज्य औसत",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.SemiBold
                                )

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "${stateSummary.totalCount}/38 Districts",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = UnitConverter.formatTemperature(stateSummary.averageTempC, tempUnit),
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Arithmetic mean calculated across current district readings. Note: Not an official IMD regional figure.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Extrema Highlights Grid: Hottest, Coolest, Rainiest, Windiest
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            stateSummary.hottest?.let { h ->
                                StatExtremumCard(
                                    titleEn = "Hottest District",
                                    titleHi = "सर्वाधिक गर्म ज़िला",
                                    district = h.district,
                                    value = UnitConverter.formatTemperature(h.tempC, tempUnit),
                                    icon = Icons.Filled.Whatshot,
                                    iconColor = AlertCritical,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onDistrictClick(h.district) }
                                )
                            }

                            stateSummary.coolest?.let { c ->
                                StatExtremumCard(
                                    titleEn = "Coolest District",
                                    titleHi = "सर्वाधिक ठंडा ज़िला",
                                    district = c.district,
                                    value = UnitConverter.formatTemperature(c.tempC, tempUnit),
                                    icon = Icons.Filled.AcUnit,
                                    iconColor = SkyBluePrimary,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onDistrictClick(c.district) }
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            stateSummary.rainiest?.let { r ->
                                StatExtremumCard(
                                    titleEn = "Rainiest District",
                                    titleHi = "सर्वाधिक वर्षा संभावना",
                                    district = r.district,
                                    value = "${r.rainProbability}%",
                                    icon = Icons.Filled.WaterDrop,
                                    iconColor = RainCyan,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onDistrictClick(r.district) }
                                )
                            }

                            stateSummary.windiest?.let { w ->
                                StatExtremumCard(
                                    titleEn = "Windiest District",
                                    titleHi = "तेज़ हवा वाला ज़िला",
                                    district = w.district,
                                    value = String.format(java.util.Locale.US, "%.1f km/h", w.windSpeedKmh),
                                    icon = Icons.Filled.Air,
                                    iconColor = SkyBlueSecondary,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onDistrictClick(w.district) }
                                )
                            }
                        }
                    }
                }

                // Regional Zone Breakdown: North vs South Bihar
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Regional Distribution • क्षेत्रीय विश्लेषण",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // North Bihar Card
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "North Bihar (उत्तर बिहार)",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = SkyBluePrimary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = stateSummary.northBiharAvgTempC?.let {
                                            UnitConverter.formatTemperature(it, tempUnit)
                                        } ?: "--",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "21 districts • Tirhut, Saran, Kosi, Purnia",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 10.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                // South Bihar Card
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "South Bihar (दक्षिण बिहार)",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = SunAmber
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = stateSummary.southBiharAvgTempC?.let {
                                            UnitConverter.formatTemperature(it, tempUnit)
                                        } ?: "--",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "17 districts • Patna, Magadh, Munger, Bhagalpur",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Top Warmest Districts
                item {
                    val warmest = allDistricts.sortedByDescending { it.tempC }.take(5)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Top 5 Warmest Districts • शीर्ष गर्म ज़िले",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            warmest.forEachIndexed { idx, item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onDistrictClick(item.district) }
                                        .padding(vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "#${idx + 1}",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = SkyBluePrimary,
                                            modifier = Modifier.width(26.dp)
                                        )
                                        Text(
                                            text = "${item.district.nameEn} (${item.district.nameHi})",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        WeatherIconView(iconType = item.condition.iconType, size = 20.dp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = UnitConverter.formatTemperature(item.tempC, tempUnit),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                if (idx < warmest.size - 1) {
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Official Meteorological Disclaimer
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Official Alert Advisory • चेतावनी सूचना",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "State aggregates displayed above represent numerical calculations across the 38 district datasets. Official meteorological warnings, heatwave alerts, and monsoon bulletins for Bihar are issued exclusively by the India Meteorological Department (IMD) / Bihar State Disaster Management Authority (BSDMA).",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatExtremumCard(
    titleEn: String,
    titleHi: String,
    district: District,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .testTag("extremum_card_${titleEn.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = titleEn,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "$titleEn • $titleHi",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${district.nameEn} (${district.nameHi})",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
