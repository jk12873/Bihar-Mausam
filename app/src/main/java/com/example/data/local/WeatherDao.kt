package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_cache WHERE districtId = :districtId LIMIT 1")
    suspend fun getCache(districtId: String): WeatherCacheEntity?

    @Query("SELECT * FROM weather_cache")
    suspend fun getAllCaches(): List<WeatherCacheEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCache(cache: WeatherCacheEntity)

    @Query("DELETE FROM weather_cache WHERE districtId = :districtId")
    suspend fun deleteCache(districtId: String)

    @Query("DELETE FROM weather_cache")
    suspend fun clearAll()
}
