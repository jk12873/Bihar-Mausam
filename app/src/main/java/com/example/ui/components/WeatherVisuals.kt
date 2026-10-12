package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.model.WeatherIconType
import com.example.ui.theme.RainCyan
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunAmber

@Composable
fun WeatherIconView(
    iconType: WeatherIconType,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .testTag("weather_icon_view"),
        contentAlignment = Alignment.Center
    ) {
        when (iconType) {
            WeatherIconType.SUNNY -> {
                Canvas(modifier = Modifier.size(size)) {
                    val center = Offset(this.size.width / 2f, this.size.height / 2f)
                    val radius = this.size.width * 0.28f
                    // Sun Disc
                    drawCircle(color = SunAmber, radius = radius, center = center)
                    // Sun Rays
                    val rayLength = this.size.width * 0.12f
                    val strokeW = this.size.width * 0.07f
                    for (i in 0 until 8) {
                        val angle = Math.toRadians((i * 45).toDouble())
                        val start = Offset(
                            (center.x + (radius + 4.dp.toPx()) * Math.cos(angle)).toFloat(),
                            (center.y + (radius + 4.dp.toPx()) * Math.sin(angle)).toFloat()
                        )
                        val end = Offset(
                            (center.x + (radius + 4.dp.toPx() + rayLength) * Math.cos(angle)).toFloat(),
                            (center.y + (radius + 4.dp.toPx() + rayLength) * Math.sin(angle)).toFloat()
                        )
                        drawLine(
                            color = SunAmber.copy(alpha = 0.85f),
                            start = start,
                            end = end,
                            strokeWidth = strokeW
                        )
                    }
                }
            }
            WeatherIconType.CLEAR_NIGHT -> {
                Icon(
                    imageVector = Icons.Filled.NightsStay,
                    contentDescription = "Clear Night",
                    tint = Color(0xFFFFD54F),
                    modifier = Modifier.size(size)
                )
            }
            WeatherIconType.PARTLY_CLOUDY_DAY -> {
                Icon(
                    imageVector = Icons.Filled.WbCloudy,
                    contentDescription = "Partly Cloudy Day",
                    tint = SkyBluePrimary,
                    modifier = Modifier.size(size)
                )
            }
            WeatherIconType.PARTLY_CLOUDY_NIGHT -> {
                Icon(
                    imageVector = Icons.Filled.Cloud,
                    contentDescription = "Partly Cloudy Night",
                    tint = Color(0xFF90A4AE),
                    modifier = Modifier.size(size)
                )
            }
            WeatherIconType.CLOUDY -> {
                Icon(
                    imageVector = Icons.Filled.Cloud,
                    contentDescription = "Cloudy",
                    tint = Color(0xFF78909C),
                    modifier = Modifier.size(size)
                )
            }
            WeatherIconType.FOG -> {
                Icon(
                    imageVector = Icons.Filled.BlurOn,
                    contentDescription = "Foggy",
                    tint = Color(0xFFB0BEC5),
                    modifier = Modifier.size(size)
                )
            }
            WeatherIconType.DRIZZLE, WeatherIconType.RAIN -> {
                Icon(
                    imageVector = Icons.Filled.WaterDrop,
                    contentDescription = "Rain",
                    tint = RainCyan,
                    modifier = Modifier.size(size)
                )
            }
            WeatherIconType.HEAVY_RAIN -> {
                Icon(
                    imageVector = Icons.Filled.Thunderstorm,
                    contentDescription = "Heavy Rain",
                    tint = SkyBluePrimary,
                    modifier = Modifier.size(size)
                )
            }
            WeatherIconType.THUNDERSTORM -> {
                Icon(
                    imageVector = Icons.Filled.FlashOn,
                    contentDescription = "Thunderstorm",
                    tint = Color(0xFFFFCA28),
                    modifier = Modifier.size(size)
                )
            }
            WeatherIconType.SNOW -> {
                Icon(
                    imageVector = Icons.Filled.AcUnit,
                    contentDescription = "Snow",
                    tint = Color(0xFF81D4FA),
                    modifier = Modifier.size(size)
                )
            }
        }
    }
}
