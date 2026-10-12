package com.example.utils

import com.example.data.local.TemperatureUnit
import com.example.data.local.WindSpeedUnit
import java.util.Locale
import kotlin.math.roundToInt

object UnitConverter {

    fun formatTemperature(tempC: Double, unit: TemperatureUnit): String {
        return when (unit) {
            TemperatureUnit.CELSIUS -> "${tempC.roundToInt()}°C"
            TemperatureUnit.FAHRENHEIT -> {
                val f = (tempC * 9.0 / 5.0) + 32.0
                "${f.roundToInt()}°F"
            }
        }
    }

    fun convertTemp(tempC: Double, unit: TemperatureUnit): Double {
        return when (unit) {
            TemperatureUnit.CELSIUS -> tempC
            TemperatureUnit.FAHRENHEIT -> (tempC * 9.0 / 5.0) + 32.0
        }
    }

    fun formatWindSpeed(speedKmh: Double, unit: WindSpeedUnit): String {
        return when (unit) {
            WindSpeedUnit.KMH -> String.format(Locale.US, "%.1f km/h", speedKmh)
            WindSpeedUnit.MS -> {
                val ms = speedKmh / 3.6
                String.format(Locale.US, "%.1f m/s", ms)
            }
        }
    }

    fun getWindDirection(deg: Double): Pair<String, String> {
        val normalized = (deg % 360 + 360) % 360
        return when {
            normalized in 22.5..67.5 -> Pair("NE", "उत्तर-पूर्व")
            normalized in 67.5..112.5 -> Pair("E", "पूर्व")
            normalized in 112.5..157.5 -> Pair("SE", "दक्षिण-पूर्व")
            normalized in 157.5..202.5 -> Pair("S", "दक्षिण")
            normalized in 202.5..247.5 -> Pair("SW", "दक्षिण-पश्चिम")
            normalized in 247.5..292.5 -> Pair("W", "पश्चिम")
            normalized in 292.5..337.5 -> Pair("NW", "उत्तर-पश्चिम")
            else -> Pair("N", "उत्तर")
        }
    }

    fun getUvCategory(uv: Double): Pair<String, String> {
        return when {
            uv < 3.0 -> Pair("Low", "कम")
            uv < 6.0 -> Pair("Moderate", "मध्यम")
            uv < 8.0 -> Pair("High", "उच्च")
            uv < 11.0 -> Pair("Very High", "अति उच्च")
            else -> Pair("Extreme", "अत्यधिक")
        }
    }
}
