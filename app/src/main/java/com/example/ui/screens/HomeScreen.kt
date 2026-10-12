package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TemperatureUnit
import com.example.data.local.WindSpeedUnit
import com.example.data.model.District
import com.example.data.model.DistrictWeather
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.WeatherResultState
import com.example.utils.UnitConverter

@Composable
fun HomeScreen(
    selectedDistrict: District,
    weatherState: WeatherResultState,
    isRefreshing: Boolean,
    tempUnit: TemperatureUnit,
    windUnit: WindSpeedUnit,
    onDistrictClick: () -> Unit,
    onRefresh: () -> Unit,
    onLocateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen")
    ) {
        when (weatherState) {
            is WeatherResultState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = SkyBluePrimary,
                            modifier = Modifier.testTag("home_loading_indicator")
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Loading ${selectedDistrict.nameEn} weather...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${selectedDistrict.nameHi} का मौसम लोड हो रहा है...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            is WeatherResultState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_error_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CloudOff,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(52.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "Unable to fetch live weather",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = weatherState.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            Button(
                                onClick = onRefresh,
                                colors = ButtonDefaults.buttonColors(containerColor = SkyBluePrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("retry_weather_button")
                            ) {
                                Icon(Icons.Filled.Refresh, contentDescription = "Retry")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Retry • पुनः प्रयास करें")
                            }
                        }
                    }
                }
            }
            is WeatherResultState.Success -> {
                HomeWeatherContent(
                    weather = weatherState.weather,
                    tempUnit = tempUnit,
                    windUnit = windUnit,
                    isRefreshing = isRefreshing,
                    onDistrictClick = onDistrictClick,
                    onRefresh = onRefresh,
                    onLocateClick = onLocateClick
                )
            }
        }
    }
}

@Composable
private fun HomeWeatherContent(
    weather: DistrictWeather,
    tempUnit: TemperatureUnit,
    windUnit: WindSpeedUnit,
    isRefreshing: Boolean,
    onDistrictClick: () -> Unit,
    onRefresh: () -> Unit,
    onLocateClick: () -> Unit
) {
    val current = weather.current
    val district = weather.district

    // Dynamic gradient based on time of day & condition
    val gradientColors = if (current.isDay) {
        listOf(
            Color(0xFF0288D1),
            Color(0xFF29B6F6),
            MaterialTheme.colorScheme.background
        )
    } else {
        listOf(
            Color(0xFF0A192F),
            Color(0xFF1E3A5F),
            MaterialTheme.colorScheme.background
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_weather_content"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Header Section with Gradient
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(gradientColors, endY = 950f))
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                Column {
                    // Top App Bar Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // District Switcher Button
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .clickable { onDistrictClick() }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("district_switcher_button")
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = "District",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${district.nameEn} (${district.nameHi})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Change district",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Right Action Buttons (Locate GPS & Refresh)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = onLocateClick,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .testTag("locate_gps_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.MyLocation,
                                    contentDescription = "Find nearest district",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            IconButton(
                                onClick = onRefresh,
                                enabled = !isRefreshing,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                                    .testTag("refresh_weather_button")
                            ) {
                                if (isRefreshing) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Filled.Refresh,
                                        contentDescription = "Refresh",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Status Pill (Live vs Cached)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (weather.isCached) Color(0xFFFFA000).copy(alpha = 0.35f) else Color(0xFF4CAF50).copy(alpha = 0.35f)
                        ) {
                            Text(
                                text = if (weather.isCached) "OFFLINE FALLBACK • ऑफ़लाइन बैकअप" else "LIVE DATA • लाइव डेटा (${current.lastUpdatedFormatted})",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${district.division} Division, Bihar",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Hero Temperature & Icon Centerpiece
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = UnitConverter.formatTemperature(current.tempC, tempUnit),
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontSize = 62.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                modifier = Modifier.testTag("current_temperature_text")
                            )

                            Text(
                                text = "${current.condition.labelEn} • ${current.condition.labelHi}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White.copy(alpha = 0.95f)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "L: ${UnitConverter.formatTemperature(current.minTempTodayC, tempUnit)}  •  H: ${UnitConverter.formatTemperature(current.maxTempTodayC, tempUnit)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }

                        WeatherIconView(
                            iconType = current.condition.iconType,
                            size = 80.dp
                        )
                    }
                }
            }
        }

        // 24-Hour Forecast Chart Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-10).dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(modifier = Modifier.padding(14.dp)) {
                    HourlyWeatherChart(
                        hourlyList = weather.hourly,
                        tempUnit = tempUnit
                    )
                }
            }
        }

        // Metrics Grid (Humidity, Wind, UV, Rain, Feels Like, Cloud)
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                WeatherMetricsGrid(
                    weather = current,
                    tempUnit = tempUnit,
                    windUnit = windUnit
                )
            }
        }

        // Sun Tracker Card
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                SunTrackerCard(
                    sunriseTime = current.sunriseTime,
                    sunsetTime = current.sunsetTime,
                    isDay = current.isDay
                )
            }
        }

        // 7-Day Outlook
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                DailyForecastCard(
                    dailyList = weather.daily,
                    tempUnit = tempUnit
                )
            }
        }

        // Legal & Meteorological Disclaimer Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Data Attribution & Disclaimer • सूचना",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Weather data provided by Open-Meteo under CC-BY 4.0. Forecasts are numerical meteorological models and may vary from local actuals. Official government alerts are issued by the India Meteorological Department (IMD).",
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
