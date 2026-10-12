package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherCacheEntity(
    @PrimaryKey
    val districtId: String,
    val jsonPayload: String,
    val cachedAtMillis: Long
)
