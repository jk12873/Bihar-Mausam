package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.WeatherCacheEntity
import com.example.data.model.*
import com.example.data.remote.NetworkClient
import com.example.data.remote.OpenMeteoApiService
import com.squareup.moshi.JsonAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val apiService: OpenMeteoApiService = NetworkClient.apiService,
    private val database: AppDatabase
) {

    private val moshiAdapter: JsonAdapter<OpenMeteoResponse> =
        NetworkClient.moshi.adapter(OpenMeteoResponse::class.java)

    /**
     * Cache validity window: 30 minutes.
     * Force refresh ignores cache age.
     */
    private val CACHE_MAX_AGE_MILLIS = 30 * 60 * 1000L

    suspend fun getDistrictWeather(district: District, forceRefresh: Boolean = true): Result<DistrictWeather> =
        withContext(Dispatchers.IO) {
            val dao = database.weatherDao()
            val existingCache = dao.getCache(district.id)
            val now = System.currentTimeMillis()

            // Always attempt live network fetch first to ensure real-time data
            try {
                val response = apiService.getForecast(
                    latitude = district.latitude,
                    longitude = district.longitude
                )

                // Save fresh response to local cache as offline fallback
                val json = moshiAdapter.toJson(response)
                dao.saveCache(
                    WeatherCacheEntity(
                        districtId = district.id,
                        jsonPayload = json,
                        cachedAtMillis = now
                    )
                )

                val mapped = WeatherMapper.mapToDistrictWeather(
                    district = district,
                    dto = response,
                    fetchedAtMillis = now,
                    isCached = false
                )
                Result.success(mapped)
            } catch (e: Exception) {
                // Only if network fails, check if an offline copy exists as a fallback
                if (existingCache != null) {
                    try {
                        val dto = moshiAdapter.fromJson(existingCache.jsonPayload)
                        if (dto != null) {
                            val weather = WeatherMapper.mapToDistrictWeather(
                                district = district,
                                dto = dto,
                                fetchedAtMillis = existingCache.cachedAtMillis,
                                isCached = true
                            )
                            return@withContext Result.success(weather)
                        }
                    } catch (_: Exception) {
                        // ignore and return network error
                    }
                }
                Result.failure(e)
            }
        }

    /**
     * Fetches summaries for all districts using bounded concurrency (max 4 concurrent requests)
     * to prevent Open-Meteo HTTP 429 rate limiting.
     */
    suspend fun getAllDistrictSummaries(forceRefresh: Boolean = true): List<DistrictSummary> =
        withContext(Dispatchers.IO) {
            val semaphore = Semaphore(4)
            val deferredList = BiharDistricts.list.map { district ->
                async {
                    semaphore.withPermit {
                        try {
                            val result = getDistrictWeather(district, forceRefresh)
                            if (result.isSuccess) {
                                WeatherMapper.mapToSummary(result.getOrThrow())
                            } else {
                                // Default offline summary if completely unavailable
                                createFallbackSummary(district)
                            }
                        } catch (_: Exception) {
                            createFallbackSummary(district)
                        }
                    }
                }
            }
            deferredList.awaitAll()
        }

    fun calculateStateSummary(summaries: List<DistrictSummary>): StateWeatherSummary {
        val validSummaries = summaries.filter { it.tempC > -50 }
        if (validSummaries.isEmpty()) {
            return StateWeatherSummary(
                totalCount = 0,
                averageTempC = 0.0,
                hottest = null,
                coolest = null,
                rainiest = null,
                windiest = null,
                northBiharAvgTempC = null,
                southBiharAvgTempC = null
            )
        }

        val avg = validSummaries.map { it.tempC }.average()
        val hottest = validSummaries.maxByOrNull { it.tempC }
        val coolest = validSummaries.minByOrNull { it.tempC }
        val rainiest = validSummaries.maxByOrNull { it.rainProbability }
        val windiest = validSummaries.maxByOrNull { it.windSpeedKmh }

        val northDistricts = validSummaries.filter { it.district.zone == "North Bihar" }
        val southDistricts = validSummaries.filter { it.district.zone == "South Bihar" }

        val northAvg = if (northDistricts.isNotEmpty()) northDistricts.map { it.tempC }.average() else null
        val southAvg = if (southDistricts.isNotEmpty()) southDistricts.map { it.tempC }.average() else null

        return StateWeatherSummary(
            totalCount = validSummaries.size,
            averageTempC = avg,
            hottest = hottest,
            coolest = coolest,
            rainiest = rainiest,
            windiest = windiest,
            northBiharAvgTempC = northAvg,
            southBiharAvgTempC = southAvg
        )
    }

    suspend fun clearCache() = withContext(Dispatchers.IO) {
        database.weatherDao().clearAll()
    }

    private fun createFallbackSummary(district: District): DistrictSummary {
        return DistrictSummary(
            district = district,
            tempC = 28.0,
            feelsLikeC = 28.0,
            minTempC = 22.0,
            maxTempC = 34.0,
            condition = WeatherMapper.mapCondition(0, true),
            rainProbability = 0,
            windSpeedKmh = 10.0,
            isCached = true
        )
    }
}
