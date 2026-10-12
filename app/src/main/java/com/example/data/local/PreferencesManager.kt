package com.example.data.local

import android.content.Context
import android.content.SharedPreferences

enum class TemperatureUnit {
    CELSIUS,
    FAHRENHEIT
}

enum class WindSpeedUnit {
    KMH,
    MS
}

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("bihar_mausam_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TEMP_UNIT = "key_temp_unit"
        private const val KEY_WIND_UNIT = "key_wind_unit"
        private const val KEY_DEFAULT_DISTRICT = "key_default_district"
        private const val KEY_LAST_DISTRICT = "key_last_district"
    }

    var temperatureUnit: TemperatureUnit
        get() {
            val name = prefs.getString(KEY_TEMP_UNIT, TemperatureUnit.CELSIUS.name)
            return try {
                TemperatureUnit.valueOf(name ?: TemperatureUnit.CELSIUS.name)
            } catch (_: Exception) {
                TemperatureUnit.CELSIUS
            }
        }
        set(value) {
            prefs.edit().putString(KEY_TEMP_UNIT, value.name).apply()
        }

    var windSpeedUnit: WindSpeedUnit
        get() {
            val name = prefs.getString(KEY_WIND_UNIT, WindSpeedUnit.KMH.name)
            return try {
                WindSpeedUnit.valueOf(name ?: WindSpeedUnit.KMH.name)
            } catch (_: Exception) {
                WindSpeedUnit.KMH
            }
        }
        set(value) {
            prefs.edit().putString(KEY_WIND_UNIT, value.name).apply()
        }

    var defaultDistrictId: String
        get() = prefs.getString(KEY_DEFAULT_DISTRICT, "patna") ?: "patna"
        set(value) {
            prefs.edit().putString(KEY_DEFAULT_DISTRICT, value).apply()
        }

    var lastDistrictId: String
        get() = prefs.getString(KEY_LAST_DISTRICT, "patna") ?: "patna"
        set(value) {
            prefs.edit().putString(KEY_LAST_DISTRICT, value).apply()
        }
}
