package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TemperatureUnit
import com.example.data.local.WindSpeedUnit
import com.example.data.model.BiharDistricts
import com.example.data.model.District
import com.example.ui.theme.AlertCritical
import com.example.ui.theme.SkyBluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    tempUnit: TemperatureUnit,
    windUnit: WindSpeedUnit,
    selectedDistrict: District,
    onToggleTempUnit: () -> Unit,
    onWindUnitChange: (WindSpeedUnit) -> Unit,
    onSelectDefaultDistrict: () -> Unit,
    onClearCache: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showClearCacheDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("settings_screen")
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Settings • सेटिंग्स",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Preferences & data attribution",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .testTag("settings_content"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Units Section
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
                            text = "Measurement Units • माप की इकाइयाँ",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Temperature Unit Switch
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleTempUnit() }
                                .padding(vertical = 8.dp)
                                .testTag("temp_unit_row"),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Temperature Unit • तापमान इकाई",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = if (tempUnit == TemperatureUnit.CELSIUS) "Celsius (°C)" else "Fahrenheit (°F)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                FilterChip(
                                    selected = tempUnit == TemperatureUnit.CELSIUS,
                                    onClick = onToggleTempUnit,
                                    label = { Text("°C") },
                                    modifier = Modifier.testTag("unit_celsius_chip")
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                FilterChip(
                                    selected = tempUnit == TemperatureUnit.FAHRENHEIT,
                                    onClick = onToggleTempUnit,
                                    label = { Text("°F") },
                                    modifier = Modifier.testTag("unit_fahrenheit_chip")
                                )
                            }
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // Wind Speed Unit
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .testTag("wind_unit_row"),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Wind Speed Unit • पवन गति इकाई",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = if (windUnit == WindSpeedUnit.KMH) "Kilometers per hour (km/h)" else "Meters per second (m/s)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                FilterChip(
                                    selected = windUnit == WindSpeedUnit.KMH,
                                    onClick = { onWindUnitChange(WindSpeedUnit.KMH) },
                                    label = { Text("km/h") },
                                    modifier = Modifier.testTag("unit_kmh_chip")
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                FilterChip(
                                    selected = windUnit == WindSpeedUnit.MS,
                                    onClick = { onWindUnitChange(WindSpeedUnit.MS) },
                                    label = { Text("m/s") },
                                    modifier = Modifier.testTag("unit_ms_chip")
                                )
                            }
                        }
                    }
                }
            }

            // Location & District Preferences
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
                            text = "District Preferences • ज़िला प्राथमिकताएँ",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectDefaultDistrict() }
                                .padding(vertical = 8.dp)
                                .testTag("default_district_row"),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Active District • वर्तमान ज़िला",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${selectedDistrict.nameEn} (${selectedDistrict.nameHi})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            OutlinedButton(
                                onClick = onSelectDefaultDistrict,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("change_district_button")
                            ) {
                                Text("Change")
                            }
                        }
                    }
                }
            }

            // Storage & Caching Section
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
                            text = "Data & Offline Storage • डेटा संग्रहण",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Offline Room Database",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Cached forecast data ensures instant offline viewing",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            TextButton(
                                onClick = { showClearCacheDialog = true },
                                colors = ButtonDefaults.textButtonColors(contentColor = AlertCritical),
                                modifier = Modifier.testTag("clear_cache_button")
                            ) {
                                Text("Clear Cache")
                            }
                        }
                    }
                }
            }

            // About & Legal Attribution Section
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
                            text = "About Bihar Mausam • ऐप परिचय",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Bihar Mausam (बिहार मौसम) is an open-access meteorological app providing comprehensive forecasts for all 38 districts of Bihar, India.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "• Weather Data: Open-Meteo API (CC-BY 4.0)\n• Timezone: Asia/Kolkata (IST)\n• Version: 1.0.0 (Production Release)\n• Architecture: MVVM with Jetpack Compose & Room",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showClearCacheDialog) {
        AlertDialog(
            onDismissRequest = { showClearCacheDialog = false },
            title = { Text("Clear Offline Cache?") },
            text = { Text("All cached district weather data will be deleted and fresh data will be downloaded from Open-Meteo.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearCache()
                        showClearCacheDialog = false
                    }
                ) {
                    Text("Clear", color = AlertCritical)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
