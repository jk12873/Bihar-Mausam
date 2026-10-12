package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SunAmber

@Composable
fun SunTrackerCard(
    sunriseTime: String,
    sunsetTime: String,
    isDay: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("sun_tracker_card"),
        shape = RoundedCornerShape(16.dp),
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.WbTwilight,
                        contentDescription = "Sun tracker",
                        tint = SunAmber,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sun Cycle • सूर्य चक्र",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDay) SunAmber.copy(alpha = 0.15f) else Color(0xFF3F51B5).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (isDay) "Daylight • दिन" else "Night • रात",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isDay) SunAmber else Color(0xFF7986CB)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Semicircular sun path arc
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(65.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(65.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    val arcRect = Size(w, h * 2)
                    val arcTopLeft = Offset(0f, 0f)

                    // Background dashed arc
                    drawArc(
                        color = Color.Gray.copy(alpha = 0.25f),
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = arcTopLeft,
                        size = arcRect,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Active solar progress arc (approx mid-day for illustration)
                    val progressAngle = if (isDay) 110f else 0f
                    if (isDay) {
                        drawArc(
                            color = SunAmber,
                            startAngle = 180f,
                            sweepAngle = progressAngle,
                            useCenter = false,
                            topLeft = arcTopLeft,
                            size = arcRect,
                            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Sun disc on arc
                        val angleRad = Math.toRadians((180.0 + progressAngle))
                        val sunX = (w / 2f) + ((w / 2f) * Math.cos(angleRad)).toFloat()
                        val sunY = (h) + ((h) * Math.sin(angleRad)).toFloat()

                        drawCircle(
                            color = SunAmber.copy(alpha = 0.35f),
                            radius = 8.dp.toPx(),
                            center = Offset(sunX, sunY)
                        )
                        drawCircle(
                            color = SunAmber,
                            radius = 5.dp.toPx(),
                            center = Offset(sunX, sunY)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sunrise and Sunset labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Sunrise • सूर्योदय",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = sunriseTime,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Sunset • सूर्यास्त",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = sunsetTime,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
