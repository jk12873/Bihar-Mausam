package com.example.data.model

data class WeatherCondition(
    val code: Int,
    val labelEn: String,
    val labelHi: String,
    val iconType: WeatherIconType,
    val isDay: Boolean = true
)

enum class WeatherIconType {
    SUNNY,
    CLEAR_NIGHT,
    PARTLY_CLOUDY_DAY,
    PARTLY_CLOUDY_NIGHT,
    CLOUDY,
    FOG,
    DRIZZLE,
    RAIN,
    HEAVY_RAIN,
    THUNDERSTORM,
    SNOW
}

data class CurrentWeather(
    val tempC: Double,
    val feelsLikeC: Double,
    val minTempTodayC: Double,
    val maxTempTodayC: Double,
    val humidityPercent: Int,
    val windSpeedKmh: Double,
    val windDirectionDeg: Double,
    val uvIndex: Double,
    val precipitationProb: Int,
    val precipitationMm: Double,
    val cloudCoverPercent: Int,
    val condition: WeatherCondition,
    val isDay: Boolean,
    val sunriseTime: String,
    val sunsetTime: String,
    val lastUpdatedFormatted: String,
    val isCached: Boolean
)

data class HourlyForecastPoint(
    val timeIso: String,
    val hourLabel: String,
    val tempC: Double,
    val rainProbability: Int,
    val condition: WeatherCondition
)

data class DailyForecastDay(
    val dateIso: String,
    val dayLabelEn: String,
    val dayLabelHi: String,
    val minTempC: Double,
    val maxTempC: Double,
    val condition: WeatherCondition,
    val rainProbMax: Int,
    val precipitationSumMm: Double,
    val uvIndexMax: Double,
    val sunriseTime: String,
    val sunsetTime: String
)

data class DistrictWeather(
    val district: District,
    val current: CurrentWeather,
    val hourly: List<HourlyForecastPoint>,
    val daily: List<DailyForecastDay>,
    val fetchedAtMillis: Long,
    val isCached: Boolean
)

data class DistrictSummary(
    val district: District,
    val tempC: Double,
    val feelsLikeC: Double,
    val minTempC: Double,
    val maxTempC: Double,
    val condition: WeatherCondition,
    val rainProbability: Int,
    val windSpeedKmh: Double,
    val isCached: Boolean
)

data class StateWeatherSummary(
    val totalCount: Int,
    val averageTempC: Double,
    val hottest: DistrictSummary?,
    val coolest: DistrictSummary?,
    val rainiest: DistrictSummary?,
    val windiest: DistrictSummary?,
    val northBiharAvgTempC: Double?,
    val southBiharAvgTempC: Double?
)
