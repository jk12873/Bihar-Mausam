package com.example

import com.example.data.model.*
import org.junit.Assert.*
import org.junit.Test

class WeatherMapperTest {

    @Test
    fun testWmoWeatherCodeMapping() {
        // Clear sky
        val clear = WeatherMapper.mapCondition(0, isDay = true)
        assertEquals("Clear Sky", clear.labelEn)
        assertEquals("साफ़ आसमान", clear.labelHi)
        assertEquals(WeatherIconType.SUNNY, clear.iconType)

        // Clear night
        val clearNight = WeatherMapper.mapCondition(0, isDay = false)
        assertEquals(WeatherIconType.CLEAR_NIGHT, clearNight.iconType)

        // Rain
        val rain = WeatherMapper.mapCondition(61, isDay = true)
        assertEquals("Rain", rain.labelEn)
        assertEquals("बारिश", rain.labelHi)
        assertEquals(WeatherIconType.RAIN, rain.iconType)

        // Thunderstorm
        val storm = WeatherMapper.mapCondition(95, isDay = true)
        assertEquals("Thunderstorm", storm.labelEn)
        assertEquals(WeatherIconType.THUNDERSTORM, storm.iconType)
    }

    @Test
    fun testOpenMeteoResponseMapping() {
        val district = BiharDistricts.getDistrict("patna")
        val currentDto = CurrentDto(
            time = "2026-10-12T12:00",
            temperature2m = 32.5,
            relativeHumidity2m = 55,
            apparentTemperature = 35.0,
            isDay = 1,
            precipitation = 0.0,
            rain = 0.0,
            showers = 0.0,
            weatherCode = 1,
            cloudCover = 15,
            windSpeed10m = 12.0,
            windDirection10m = 85.0
        )
        val dailyDto = DailyDto(
            time = listOf("2026-10-12"),
            weatherCode = listOf(1),
            temperature2mMax = listOf(36.0),
            temperature2mMin = listOf(24.0),
            sunrise = listOf("2026-10-12T05:48"),
            sunset = listOf("2026-10-12T17:35"),
            uvIndexMax = listOf(7.2),
            precipitationProbabilityMax = listOf(10),
            precipitationSum = listOf(0.0)
        )
        val mockResponse = OpenMeteoResponse(
            latitude = 25.5941,
            longitude = 85.1376,
            timezone = "Asia/Kolkata",
            current = currentDto,
            hourly = null,
            daily = dailyDto
        )

        val result = WeatherMapper.mapToDistrictWeather(
            district = district,
            dto = mockResponse,
            fetchedAtMillis = System.currentTimeMillis(),
            isCached = false
        )

        assertEquals("Patna", result.district.nameEn)
        assertEquals(32.5, result.current.tempC, 0.01)
        assertEquals(35.0, result.current.feelsLikeC, 0.01)
        assertEquals(55, result.current.humidityPercent)
        assertEquals(36.0, result.current.maxTempTodayC, 0.01)
        assertEquals(24.0, result.current.minTempTodayC, 0.01)
        assertFalse(result.isCached)
    }
}
