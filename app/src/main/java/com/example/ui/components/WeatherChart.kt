package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TemperatureUnit
import com.example.data.model.HourlyForecastPoint
import com.example.ui.theme.RainCyan
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.theme.SunAmber
import com.example.utils.UnitConverter
import kotlin.math.roundToInt

@Composable
fun HourlyWeatherChart(
    hourlyList: List<HourlyForecastPoint>,
    tempUnit: TemperatureUnit,
    modifier: Modifier = Modifier
) {
    if (hourlyList.isEmpty()) return

    val scrollState = rememberScrollState()
    val pointWidthDp = 58.dp
    val totalWidthDp = pointWidthDp * hourlyList.size.coerceAtLeast(1)

    val temps = hourlyList.map { UnitConverter.convertTemp(it.tempC, tempUnit) }
    val minT = temps.minOrNull() ?: 20.0
    val maxT = temps.maxOrNull() ?: 35.0
    val range = if (maxT - minT < 2.0) 2.0 else (maxT - minT)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("hourly_weather_chart")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "24-Hour Forecast • 24 घंटे का पूर्वानुमान",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Scroll ➔",
                style = MaterialTheme.typography.labelSmall,
                color = SkyBluePrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .width(totalWidthDp)
                    .padding(vertical = 4.dp)
            ) {
                // Interactive Custom Canvas Chart
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) {
                    val widthPx = size.width
                    val heightPx = size.height
                    val stepPx = widthPx / hourlyList.size.toFloat()

                    val chartTop = 32.dp.toPx()
                    val chartBottom = heightPx - 24.dp.toPx()
                    val usableHeight = chartBottom - chartTop

                    val points = mutableListOf<Offset>()
                    for (i in hourlyList.indices) {
                        val t = temps[i]
                        val norm = ((t - minT) / range).toFloat()
                        val y = chartBottom - (norm * usableHeight)
                        val x = (i * stepPx) + (stepPx / 2f)
                        points.add(Offset(x, y))
                    }

                    if (points.size >= 2) {
                        // Area fill under curve
                        val fillPath = Path().apply {
                            moveTo(points[0].x, points[0].y)
                            for (i in 0 until points.size - 1) {
                                val p0 = points[i]
                                val p1 = points[i + 1]
                                val midX = (p0.x + p1.x) / 2f
                                cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
                            }
                            lineTo(points.last().x, chartBottom + 10.dp.toPx())
                            lineTo(points.first().x, chartBottom + 10.dp.toPx())
                            close()
                        }

                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    SkyBluePrimary.copy(alpha = 0.35f),
                                    SkyBluePrimary.copy(alpha = 0.03f)
                                ),
                                startY = chartTop,
                                endY = chartBottom
                            )
                        )

                        // Smooth Curve Line
                        val strokePath = Path().apply {
                            moveTo(points[0].x, points[0].y)
                            for (i in 0 until points.size - 1) {
                                val p0 = points[i]
                                val p1 = points[i + 1]
                                val midX = (p0.x + p1.x) / 2f
                                cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
                            }
                        }

                        drawPath(
                            path = strokePath,
                            color = SkyBluePrimary,
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }

                    // Dots & Temperature Text on points
                    val paint = android.graphics.Paint().apply {
                        color = android.graphics.Color.DKGRAY
                        textSize = 12.sp.toPx()
                        textAlign = android.graphics.Paint.Align.CENTER
                        isAntiAlias = true
                        typeface = android.graphics.Typeface.DEFAULT_BOLD
                    }

                    for (i in points.indices) {
                        val pt = points[i]
                        // Outer glowing circle
                        drawCircle(
                            color = SkyBluePrimary.copy(alpha = 0.25f),
                            radius = 6.dp.toPx(),
                            center = pt
                        )
                        // Inner circle
                        drawCircle(
                            color = Color.White,
                            radius = 3.5.dp.toPx(),
                            center = pt
                        )
                        drawCircle(
                            color = SkyBluePrimary,
                            radius = 2.dp.toPx(),
                            center = pt
                        )

                        // Temperature text
                        val tempText = "${temps[i].roundToInt()}°"
                        drawContext.canvas.nativeCanvas.drawText(
                            tempText,
                            pt.x,
                            pt.y - 10.dp.toPx(),
                            paint
                        )
                    }
                }

                // Row of hours & weather icons & rain prob
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    hourlyList.forEach { item ->
                        Column(
                            modifier = Modifier
                                .width(pointWidthDp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            WeatherIconView(
                                iconType = item.condition.iconType,
                                size = 20.dp
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = item.hourLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            // Rain chance indicator badge if > 0%
                            if (item.rainProbability > 0) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(
                                            color = RainCyan.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 3.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "${item.rainProbability}%",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = RainCyan
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
