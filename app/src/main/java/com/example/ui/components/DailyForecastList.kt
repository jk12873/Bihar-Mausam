package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TemperatureUnit
import com.example.data.model.DailyForecastDay
import com.example.ui.theme.RainCyan
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunAmber
import com.example.utils.UnitConverter
import kotlin.math.roundToInt

@Composable
fun DailyForecastCard(
    dailyList: List<DailyForecastDay>,
    tempUnit: TemperatureUnit,
    modifier: Modifier = Modifier
) {
    if (dailyList.isEmpty()) return

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_forecast_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "7-Day Outlook • 7 दिनों का मौसम",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Find absolute min and max across all 7 days for relative temp bar scaling
            val minAll = dailyList.minOfOrNull { it.minTempC } ?: 20.0
            val maxAll = dailyList.maxOfOrNull { it.maxTempC } ?: 35.0
            val totalSpan = if (maxAll - minAll < 1.0) 1.0 else (maxAll - minAll)

            dailyList.forEachIndexed { index, day ->
                DailyForecastRow(
                    day = day,
                    tempUnit = tempUnit,
                    globalMin = minAll,
                    globalMax = maxAll,
                    globalSpan = totalSpan
                )
                if (index < dailyList.size - 1) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DailyForecastRow(
    day: DailyForecastDay,
    tempUnit: TemperatureUnit,
    globalMin: Double,
    globalMax: Double,
    globalSpan: Double
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .padding(vertical = 4.dp)
            .testTag("daily_row_${day.dateIso}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Day label (English + Hindi)
            Column(modifier = Modifier.width(80.dp)) {
                Text(
                    text = day.dayLabelEn,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = day.dayLabelHi,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp
                )
            }

            // Weather icon + Rain probability
            Row(
                modifier = Modifier.width(68.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                WeatherIconView(iconType = day.condition.iconType, size = 26.dp)
                if (day.rainProbMax > 0) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${day.rainProbMax}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = RainCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Temperature Visual Bar & Values
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                // Min Temp
                Text(
                    text = UnitConverter.formatTemperature(day.minTempC, tempUnit),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    modifier = Modifier.width(42.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Relative Temperature bar
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                ) {
                    val leftFraction = ((day.minTempC - globalMin) / globalSpan).toFloat().coerceIn(0f, 1f)
                    val rightFraction = ((day.maxTempC - globalMin) / globalSpan).toFloat().coerceIn(0f, 1f)
                    val fillFraction = (rightFraction - leftFraction).coerceAtLeast(0.1f)

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(fraction = fillFraction)
                            .clip(RoundedCornerShape(3.dp))
                            .background(SkyBluePrimary)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Max Temp
                Text(
                    text = UnitConverter.formatTemperature(day.maxTempC, tempUnit),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 13.sp,
                    modifier = Modifier.width(42.dp)
                )

                Icon(
                    imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = "Expand day details",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Expandable daily details
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(animationSpec = tween(200)),
            exit = shrinkVertically(animationSpec = tween(200))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 8.dp, end = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(10.dp)
            ) {
                Text(
                    text = "${day.condition.labelEn} • ${day.condition.labelHi}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = SkyBluePrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Rain Sum: ${day.precipitationSumMm} mm",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "UV Max: ${day.uvIndexMax}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "🌅 ${day.sunriseTime}  🌇 ${day.sunsetTime}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
