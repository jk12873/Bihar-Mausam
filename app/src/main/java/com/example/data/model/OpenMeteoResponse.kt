package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenMeteoResponse(
    @Json(name = "latitude") val latitude: Double?,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "timezone") val timezone: String?,
    @Json(name = "current") val current: CurrentDto?,
    @Json(name = "hourly") val hourly: HourlyDto?,
    @Json(name = "daily") val daily: DailyDto?
)

@JsonClass(generateAdapter = true)
data class CurrentDto(
    @Json(name = "time") val time: String?,
    @Json(name = "temperature_2m") val temperature2m: Double?,
    @Json(name = "relative_humidity_2m") val relativeHumidity2m: Int?,
    @Json(name = "apparent_temperature") val apparentTemperature: Double?,
    @Json(name = "is_day") val isDay: Int?,
    @Json(name = "precipitation") val precipitation: Double?,
    @Json(name = "rain") val rain: Double?,
    @Json(name = "showers") val showers: Double?,
    @Json(name = "weather_code") val weatherCode: Int?,
    @Json(name = "cloud_cover") val cloudCover: Int?,
    @Json(name = "wind_speed_10m") val windSpeed10m: Double?,
    @Json(name = "wind_direction_10m") val windDirection10m: Double?
)

@JsonClass(generateAdapter = true)
data class HourlyDto(
    @Json(name = "time") val time: List<String>?,
    @Json(name = "temperature_2m") val temperature2m: List<Double?>?,
    @Json(name = "relative_humidity_2m") val relativeHumidity2m: List<Int?>?,
    @Json(name = "precipitation_probability") val precipitationProbability: List<Int?>?,
    @Json(name = "precipitation") val precipitation: List<Double?>?,
    @Json(name = "weather_code") val weatherCode: List<Int?>?,
    @Json(name = "wind_speed_10m") val windSpeed10m: List<Double?>?
)

@JsonClass(generateAdapter = true)
data class DailyDto(
    @Json(name = "time") val time: List<String>?,
    @Json(name = "weather_code") val weatherCode: List<Int?>?,
    @Json(name = "temperature_2m_max") val temperature2mMax: List<Double?>?,
    @Json(name = "temperature_2m_min") val temperature2mMin: List<Double?>?,
    @Json(name = "sunrise") val sunrise: List<String>?,
    @Json(name = "sunset") val sunset: List<String>?,
    @Json(name = "uv_index_max") val uvIndexMax: List<Double?>?,
    @Json(name = "precipitation_probability_max") val precipitationProbabilityMax: List<Int?>?,
    @Json(name = "precipitation_sum") val precipitationSum: List<Double?>?
)
