package com.example

import com.example.data.local.TemperatureUnit
import com.example.data.local.WindSpeedUnit
import com.example.utils.UnitConverter
import org.junit.Assert.assertEquals
import org.junit.Test

class UnitConverterTest {

    @Test
    fun testCelsiusToFahrenheitConversion() {
        assertEquals("0°C", UnitConverter.formatTemperature(0.0, TemperatureUnit.CELSIUS))
        assertEquals("32°F", UnitConverter.formatTemperature(0.0, TemperatureUnit.FAHRENHEIT))

        assertEquals("100°C", UnitConverter.formatTemperature(100.0, TemperatureUnit.CELSIUS))
        assertEquals("212°F", UnitConverter.formatTemperature(100.0, TemperatureUnit.FAHRENHEIT))

        assertEquals("30°C", UnitConverter.formatTemperature(30.0, TemperatureUnit.CELSIUS))
        assertEquals("86°F", UnitConverter.formatTemperature(30.0, TemperatureUnit.FAHRENHEIT))
    }

    @Test
    fun testWindSpeedConversion() {
        assertEquals("36.0 km/h", UnitConverter.formatWindSpeed(36.0, WindSpeedUnit.KMH))
        assertEquals("10.0 m/s", UnitConverter.formatWindSpeed(36.0, WindSpeedUnit.MS))
    }

    @Test
    fun testWindDirection() {
        val north = UnitConverter.getWindDirection(0.0)
        assertEquals("N", north.first)
        assertEquals("उत्तर", north.second)

        val east = UnitConverter.getWindDirection(90.0)
        assertEquals("E", east.first)
        assertEquals("पूर्व", east.second)

        val south = UnitConverter.getWindDirection(180.0)
        assertEquals("S", south.first)
        assertEquals("दक्षिण", south.second)

        val west = UnitConverter.getWindDirection(270.0)
        assertEquals("W", west.first)
        assertEquals("पश्चिम", west.second)
    }

    @Test
    fun testUvCategoryClassification() {
        val low = UnitConverter.getUvCategory(2.0)
        assertEquals("Low", low.first)

        val moderate = UnitConverter.getUvCategory(5.0)
        assertEquals("Moderate", moderate.first)

        val veryHigh = UnitConverter.getUvCategory(9.5)
        assertEquals("Very High", veryHigh.first)

        val extreme = UnitConverter.getUvCategory(12.0)
        assertEquals("Extreme", extreme.first)
    }
}
