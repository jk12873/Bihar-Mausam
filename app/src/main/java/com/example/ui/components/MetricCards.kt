package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TemperatureUnit
import com.example.data.local.WindSpeedUnit
import com.example.data.model.CurrentWeather
import com.example.ui.theme.*
import com.example.utils.UnitConverter

@Composable
fun WeatherMetricsGrid(
    weather: CurrentWeather,
    tempUnit: TemperatureUnit,
    windUnit: WindSpeedUnit,
    modifier: Modifier = Modifier
) {
    val windDir = UnitConverter.getWindDirection(weather.windDirectionDeg)
    val uvCategory = UnitConverter.getUvCategory(weather.uvIndex)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("weather_metrics_grid"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Row 1: Feels Like & Humidity
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                titleEn = "Feels Like",
                titleHi = "महसूस होता है",
                value = UnitConverter.formatTemperature(weather.feelsLikeC, tempUnit),
                subtitle = "Actual: ${UnitConverter.formatTemperature(weather.tempC, tempUnit)}",
                icon = Icons.Filled.Thermostat,
                iconColor = SunAmber,
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                titleEn = "Humidity",
                titleHi = "नमी / आर्द्रता",
                value = "${weather.humidityPercent}%",
                subtitle = if (weather.humidityPercent > 70) "Humid • उमस" else "Comfortable",
                icon = Icons.Filled.WaterDrop,
                iconColor = RainCyan,
                modifier = Modifier.weight(1f)
            )
        }

        // Row 2: Wind & UV Index
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                titleEn = "Wind",
                titleHi = "हवा की गति",
                value = UnitConverter.formatWindSpeed(weather.windSpeedKmh, windUnit),
                subtitle = "${windDir.first} (${windDir.second})",
                icon = Icons.Filled.Air,
                iconColor = SkyBluePrimary,
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                titleEn = "UV Index",
                titleHi = "यूवी इंडेक्स",
                value = String.format(java.util.Locale.US, "%.1f", weather.uvIndex),
                subtitle = "${uvCategory.first} (${uvCategory.second})",
                icon = Icons.Filled.WbSunny,
                iconColor = if (weather.uvIndex >= 8) AlertCritical else if (weather.uvIndex >= 6) AlertWarning else StatusGood,
                modifier = Modifier.weight(1f)
            )
        }

        // Row 3: Rain Probability & Cloud Cover
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard(
                titleEn = "Rain Chance",
                titleHi = "बारिश की संभावना",
                value = "${weather.precipitationProb}%",
                subtitle = if (weather.precipitationMm > 0) "${weather.precipitationMm} mm expected" else "No rain expected",
                icon = Icons.Filled.Umbrella,
                iconColor = SkyBlueSecondary,
                modifier = Modifier.weight(1f)
            )

            MetricCard(
                titleEn = "Cloud Cover",
                titleHi = "बादल छाए रहना",
                value = "${weather.cloudCoverPercent}%",
                subtitle = if (weather.cloudCoverPercent > 60) "Overcast • बादलमय" else "Clear sky • साफ़",
                icon = Icons.Filled.Cloud,
                iconColor = Color(0xFF78909C),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MetricCard(
    titleEn: String,
    titleHi: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .testTag("metric_card_${titleEn.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = titleEn,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "$titleEn • $titleHi",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
    }
}
