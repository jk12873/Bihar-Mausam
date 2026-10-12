package com.example.data.model

import java.text.SimpleDateFormat
import java.util.*

object WeatherMapper {

    fun mapCondition(weatherCode: Int, isDay: Boolean): WeatherCondition {
        return when (weatherCode) {
            0 -> if (isDay) {
                WeatherCondition(0, "Clear Sky", "साफ़ आसमान", WeatherIconType.SUNNY, true)
            } else {
                WeatherCondition(0, "Clear Night", "साफ़ रात", WeatherIconType.CLEAR_NIGHT, false)
            }
            1 -> if (isDay) {
                WeatherCondition(1, "Mainly Clear", "मुख्य रूप से साफ़", WeatherIconType.SUNNY, true)
            } else {
                WeatherCondition(1, "Mainly Clear", "मुख्य रूप से साफ़", WeatherIconType.CLEAR_NIGHT, false)
            }
            2 -> if (isDay) {
                WeatherCondition(2, "Partly Cloudy", "आंशिक बादल", WeatherIconType.PARTLY_CLOUDY_DAY, true)
            } else {
                WeatherCondition(2, "Partly Cloudy", "आंशिक बादल", WeatherIconType.PARTLY_CLOUDY_NIGHT, false)
            }
            3 -> WeatherCondition(3, "Overcast", "घने बादल", WeatherIconType.CLOUDY, isDay)
            45, 48 -> WeatherCondition(weatherCode, "Foggy", "कोहरा", WeatherIconType.FOG, isDay)
            51, 53, 55 -> WeatherCondition(weatherCode, "Drizzle", "बूंदाबांदी", WeatherIconType.DRIZZLE, isDay)
            56, 57 -> WeatherCondition(weatherCode, "Freezing Drizzle", "शीत बूंदाबांदी", WeatherIconType.DRIZZLE, isDay)
            61, 63 -> WeatherCondition(weatherCode, "Rain", "बारिश", WeatherIconType.RAIN, isDay)
            65 -> WeatherCondition(weatherCode, "Heavy Rain", "भारी बारिश", WeatherIconType.HEAVY_RAIN, isDay)
            66, 67 -> WeatherCondition(weatherCode, "Freezing Rain", "शीत वर्षा", WeatherIconType.RAIN, isDay)
            71, 73, 75, 77 -> WeatherCondition(weatherCode, "Snow", "बर्फबारी", WeatherIconType.SNOW, isDay)
            80, 81 -> WeatherCondition(weatherCode, "Showers", "बौछारें", WeatherIconType.RAIN, isDay)
            82 -> WeatherCondition(weatherCode, "Heavy Showers", "मूसलाधार बौछार", WeatherIconType.HEAVY_RAIN, isDay)
            85, 86 -> WeatherCondition(weatherCode, "Snow Showers", "बर्फीली बौछार", WeatherIconType.SNOW, isDay)
            95 -> WeatherCondition(95, "Thunderstorm", "गरज के साथ आंधी", WeatherIconType.THUNDERSTORM, isDay)
            96, 99 -> WeatherCondition(weatherCode, "Severe Storm", "तूफान और ओलावृष्टि", WeatherIconType.THUNDERSTORM, isDay)
            else -> WeatherCondition(weatherCode, "Fair", "सामान्य", if (isDay) WeatherIconType.SUNNY else WeatherIconType.CLEAR_NIGHT, isDay)
        }
    }

    fun mapToDistrictWeather(
        district: District,
        dto: OpenMeteoResponse,
        fetchedAtMillis: Long,
        isCached: Boolean
    ): DistrictWeather {
        val currentDto = dto.current
        val isDay = (currentDto?.isDay ?: 1) == 1
        val weatherCode = currentDto?.weatherCode ?: 0
        val condition = mapCondition(weatherCode, isDay)

        // Today's min/max from daily
        val todayMax = dto.daily?.temperature2mMax?.firstOrNull() ?: currentDto?.temperature2m ?: 30.0
        val todayMin = dto.daily?.temperature2mMin?.firstOrNull() ?: ((currentDto?.temperature2m ?: 30.0) - 6.0)
        val sunrise = dto.daily?.sunrise?.firstOrNull()?.let { formatTimeOnly(it) } ?: "05:45 AM"
        val sunset = dto.daily?.sunset?.firstOrNull()?.let { formatTimeOnly(it) } ?: "06:15 PM"
        val uvMax = dto.daily?.uvIndexMax?.firstOrNull() ?: 5.0
        val rainProb = dto.daily?.precipitationProbabilityMax?.firstOrNull() ?: 0
        val rainMm = dto.daily?.precipitationSum?.firstOrNull() ?: (currentDto?.precipitation ?: 0.0)

        val lastUpdatedFormatted = formatTimestamp(fetchedAtMillis)

        val currentWeather = CurrentWeather(
            tempC = currentDto?.temperature2m ?: 30.0,
            feelsLikeC = currentDto?.apparentTemperature ?: (currentDto?.temperature2m ?: 30.0),
            minTempTodayC = todayMin,
            maxTempTodayC = todayMax,
            humidityPercent = currentDto?.relativeHumidity2m ?: 60,
            windSpeedKmh = currentDto?.windSpeed10m ?: 10.0,
            windDirectionDeg = currentDto?.windDirection10m ?: 90.0,
            uvIndex = uvMax,
            precipitationProb = rainProb,
            precipitationMm = rainMm,
            cloudCoverPercent = currentDto?.cloudCover ?: 20,
            condition = condition,
            isDay = isDay,
            sunriseTime = sunrise,
            sunsetTime = sunset,
            lastUpdatedFormatted = lastUpdatedFormatted,
            isCached = isCached
        )

        // Map next 24 hours
        val hourlyList = mutableListOf<HourlyForecastPoint>()
        val hourlyTimes = dto.hourly?.time ?: emptyList()
        val hourlyTemps = dto.hourly?.temperature2m ?: emptyList()
        val hourlyProbs = dto.hourly?.precipitationProbability ?: emptyList()
        val hourlyCodes = dto.hourly?.weatherCode ?: emptyList()

        // Find current hour index or start from index 0 up to 24 items
        val startIndex = findCurrentHourIndex(hourlyTimes)
        val count = minOf(24, hourlyTimes.size - startIndex)

        for (i in 0 until count) {
            val idx = startIndex + i
            val timeIso = hourlyTimes.getOrNull(idx) ?: ""
            val temp = hourlyTemps.getOrNull(idx) ?: currentWeather.tempC
            val prob = hourlyProbs.getOrNull(idx) ?: 0
            val code = hourlyCodes.getOrNull(idx) ?: 0
            val hourLabel = formatHourLabel(timeIso, i == 0)
            val hIsDay = isHourDaytime(timeIso)
            val hCond = mapCondition(code, hIsDay)

            hourlyList.add(
                HourlyForecastPoint(
                    timeIso = timeIso,
                    hourLabel = hourLabel,
                    tempC = temp,
                    rainProbability = prob,
                    condition = hCond
                )
            )
        }

        // Map 7 daily forecasts
        val dailyList = mutableListOf<DailyForecastDay>()
        val dailyTimes = dto.daily?.time ?: emptyList()
        val dailyCodes = dto.daily?.weatherCode ?: emptyList()
        val dailyMaxs = dto.daily?.temperature2mMax ?: emptyList()
        val dailyMins = dto.daily?.temperature2mMin ?: emptyList()
        val dailySunrises = dto.daily?.sunrise ?: emptyList()
        val dailySunsets = dto.daily?.sunset ?: emptyList()
        val dailyUvs = dto.daily?.uvIndexMax ?: emptyList()
        val dailyRainProbs = dto.daily?.precipitationProbabilityMax ?: emptyList()
        val dailyRainSums = dto.daily?.precipitationSum ?: emptyList()

        for (i in 0 until minOf(7, dailyTimes.size)) {
            val dateIso = dailyTimes.getOrNull(i) ?: ""
            val code = dailyCodes.getOrNull(i) ?: 0
            val maxT = dailyMaxs.getOrNull(i) ?: 32.0
            val minT = dailyMins.getOrNull(i) ?: 22.0
            val sr = dailySunrises.getOrNull(i)?.let { formatTimeOnly(it) } ?: "05:45 AM"
            val ss = dailySunsets.getOrNull(i)?.let { formatTimeOnly(it) } ?: "06:15 PM"
            val uv = dailyUvs.getOrNull(i) ?: 5.0
            val rp = dailyRainProbs.getOrNull(i) ?: 0
            val rs = dailyRainSums.getOrNull(i) ?: 0.0

            val dayLabels = formatDayLabels(dateIso, i == 0)

            dailyList.add(
                DailyForecastDay(
                    dateIso = dateIso,
                    dayLabelEn = dayLabels.first,
                    dayLabelHi = dayLabels.second,
                    minTempC = minT,
                    maxTempC = maxT,
                    condition = mapCondition(code, true),
                    rainProbMax = rp,
                    precipitationSumMm = rs,
                    uvIndexMax = uv,
                    sunriseTime = sr,
                    sunsetTime = ss
                )
            )
        }

        return DistrictWeather(
            district = district,
            current = currentWeather,
            hourly = hourlyList,
            daily = dailyList,
            fetchedAtMillis = fetchedAtMillis,
            isCached = isCached
        )
    }

    fun mapToSummary(districtWeather: DistrictWeather): DistrictSummary {
        val cur = districtWeather.current
        return DistrictSummary(
            district = districtWeather.district,
            tempC = cur.tempC,
            feelsLikeC = cur.feelsLikeC,
            minTempC = cur.minTempTodayC,
            maxTempC = cur.maxTempTodayC,
            condition = cur.condition,
            rainProbability = cur.precipitationProb,
            windSpeedKmh = cur.windSpeedKmh,
            isCached = districtWeather.isCached
        )
    }

    private fun findCurrentHourIndex(times: List<String>): Int {
        if (times.isEmpty()) return 0
        try {
            val nowSdf = SimpleDateFormat("yyyy-MM-dd'T'HH", Locale.US)
            nowSdf.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            val currentHourPrefix = nowSdf.format(Date())
            val idx = times.indexOfFirst { it.startsWith(currentHourPrefix) }
            return if (idx >= 0) idx else 0
        } catch (_: Exception) {
            return 0
        }
    }

    private fun formatHourLabel(isoString: String, isFirst: Boolean): String {
        if (isFirst) return "Now"
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US)
            val date = parser.parse(isoString) ?: return isoString
            val formatter = SimpleDateFormat("ha", Locale.US)
            formatter.format(date).lowercase(Locale.US)
        } catch (_: Exception) {
            isoString.substringAfter("T", isoString)
        }
    }

    private fun isHourDaytime(isoString: String): Boolean {
        return try {
            val hour = isoString.substringAfter("T").substringBefore(":").toInt()
            hour in 6..18
        } catch (_: Exception) {
            true
        }
    }

    private fun formatTimeOnly(isoString: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.US)
            val date = parser.parse(isoString) ?: return isoString
            val formatter = SimpleDateFormat("hh:mm a", Locale.US)
            formatter.format(date)
        } catch (_: Exception) {
            isoString
        }
    }

    private fun formatDayLabels(dateIso: String, isToday: Boolean): Pair<String, String> {
        if (isToday) return Pair("Today", "आज")
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = parser.parse(dateIso) ?: return Pair(dateIso, dateIso)
            val enFormatter = SimpleDateFormat("EEE", Locale.US)
            val enDay = enFormatter.format(date)
            val hiDay = when (enDay.uppercase(Locale.US)) {
                "SUN" -> "रवि"
                "MON" -> "सोम"
                "TUE" -> "मंगल"
                "WED" -> "बुध"
                "THU" -> "गुरु"
                "FRI" -> "शुक्र"
                "SAT" -> "शनि"
                else -> enDay
            }
            Pair(enDay, hiDay)
        } catch (_: Exception) {
            Pair(dateIso, dateIso)
        }
    }

    private fun formatTimestamp(millis: Long): String {
        return try {
            val sdf = SimpleDateFormat("hh:mm a", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            sdf.format(Date(millis))
        } catch (_: Exception) {
            "Recent"
        }
    }
}
