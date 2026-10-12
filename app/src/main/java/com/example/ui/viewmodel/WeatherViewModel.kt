package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.PreferencesManager
import com.example.data.local.TemperatureUnit
import com.example.data.local.WindSpeedUnit
import com.example.data.model.*
import com.example.data.repository.WeatherRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface WeatherResultState {
    data object Loading : WeatherResultState
    data class Success(val weather: DistrictWeather) : WeatherResultState
    data class Error(val message: String, val cachedWeather: DistrictWeather? = null) : WeatherResultState
}

enum class DistrictFilter {
    ALL,
    RAINY,
    CLEAR,
    CLOUDY,
    HOT // > 35°C
}

enum class DistrictSort {
    NAME_ASC,
    TEMP_DESC,
    TEMP_ASC,
    RAIN_DESC
}

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager = PreferencesManager(application)
    private val database = AppDatabase.getDatabase(application)
    private val repository = WeatherRepository(database = database)

    private val _selectedDistrict = MutableStateFlow(
        BiharDistricts.getDistrict(preferencesManager.lastDistrictId)
    )
    val selectedDistrict: StateFlow<District> = _selectedDistrict.asStateFlow()

    private val _currentWeatherState = MutableStateFlow<WeatherResultState>(WeatherResultState.Loading)
    val currentWeatherState: StateFlow<WeatherResultState> = _currentWeatherState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _allDistricts = MutableStateFlow<List<DistrictSummary>>(emptyList())
    val allDistricts: StateFlow<List<DistrictSummary>> = _allDistricts.asStateFlow()

    private val _isLoadingDistricts = MutableStateFlow(false)
    val isLoadingDistricts: StateFlow<Boolean> = _isLoadingDistricts.asStateFlow()

    private val _stateSummary = MutableStateFlow<StateWeatherSummary?>(null)
    val stateSummary: StateFlow<StateWeatherSummary?> = _stateSummary.asStateFlow()

    private val _tempUnit = MutableStateFlow(preferencesManager.temperatureUnit)
    val tempUnit: StateFlow<TemperatureUnit> = _tempUnit.asStateFlow()

    private val _windUnit = MutableStateFlow(preferencesManager.windSpeedUnit)
    val windUnit: StateFlow<WindSpeedUnit> = _windUnit.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow(DistrictFilter.ALL)
    val selectedFilter: StateFlow<DistrictFilter> = _selectedFilter.asStateFlow()

    private val _sortOption = MutableStateFlow(DistrictSort.NAME_ASC)
    val sortOption: StateFlow<DistrictSort> = _sortOption.asStateFlow()

    val filteredDistricts: StateFlow<List<DistrictSummary>> = combine(
        _allDistricts,
        _searchQuery,
        _selectedFilter,
        _sortOption
    ) { list, query, filter, sort ->
        var result = list

        // Filter by Search Query (supports English and Hindi)
        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            result = result.filter { summary ->
                summary.district.nameEn.lowercase().contains(q) ||
                        summary.district.nameHi.contains(query.trim()) ||
                        summary.district.division.lowercase().contains(q)
            }
        }

        // Filter by Category
        result = when (filter) {
            DistrictFilter.ALL -> result
            DistrictFilter.RAINY -> result.filter { it.rainProbability > 30 || it.condition.iconType == WeatherIconType.RAIN || it.condition.iconType == WeatherIconType.HEAVY_RAIN }
            DistrictFilter.CLEAR -> result.filter { it.condition.iconType == WeatherIconType.SUNNY || it.condition.iconType == WeatherIconType.CLEAR_NIGHT }
            DistrictFilter.CLOUDY -> result.filter { it.condition.iconType == WeatherIconType.CLOUDY || it.condition.iconType == WeatherIconType.PARTLY_CLOUDY_DAY }
            DistrictFilter.HOT -> result.filter { it.tempC >= 35.0 }
        }

        // Sort
        result = when (sort) {
            DistrictSort.NAME_ASC -> result.sortedBy { it.district.nameEn }
            DistrictSort.TEMP_DESC -> result.sortedByDescending { it.tempC }
            DistrictSort.TEMP_ASC -> result.sortedBy { it.tempC }
            DistrictSort.RAIN_DESC -> result.sortedByDescending { it.rainProbability }
        }

        result
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadWeatherForDistrict(_selectedDistrict.value, forceRefresh = true)
        loadAllDistricts(forceRefresh = true)
    }

    fun selectDistrict(district: District) {
        _selectedDistrict.value = district
        preferencesManager.lastDistrictId = district.id
        loadWeatherForDistrict(district, forceRefresh = true)
    }

    fun refreshCurrentWeather() {
        viewModelScope.launch {
            _isRefreshing.value = true
            loadWeatherForDistrict(_selectedDistrict.value, forceRefresh = true)
            _isRefreshing.value = false
        }
    }

    fun loadWeatherForDistrict(district: District, forceRefresh: Boolean = true) {
        viewModelScope.launch {
            if (_currentWeatherState.value !is WeatherResultState.Success) {
                _currentWeatherState.value = WeatherResultState.Loading
            }
            val result = repository.getDistrictWeather(district, forceRefresh)
            if (result.isSuccess) {
                val weather = result.getOrThrow()
                _currentWeatherState.value = WeatherResultState.Success(weather)
            } else {
                val errorMsg = result.exceptionOrNull()?.localizedMessage ?: "Unable to load live forecast"
                _currentWeatherState.value = WeatherResultState.Error(errorMsg)
            }
        }
    }

    fun loadAllDistricts(forceRefresh: Boolean = true) {
        viewModelScope.launch {
            _isLoadingDistricts.value = true
            val summaries = repository.getAllDistrictSummaries(forceRefresh)
            _allDistricts.value = summaries
            _stateSummary.value = repository.calculateStateSummary(summaries)
            _isLoadingDistricts.value = false
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilter(filter: DistrictFilter) {
        _selectedFilter.value = filter
    }

    fun setSort(sort: DistrictSort) {
        _sortOption.value = sort
    }

    fun toggleTempUnit() {
        val next = if (_tempUnit.value == TemperatureUnit.CELSIUS) TemperatureUnit.FAHRENHEIT else TemperatureUnit.CELSIUS
        _tempUnit.value = next
        preferencesManager.temperatureUnit = next
    }

    fun setWindUnit(unit: WindSpeedUnit) {
        _windUnit.value = unit
        preferencesManager.windSpeedUnit = unit
    }

    fun selectNearestDistrict(lat: Double, lon: Double) {
        val nearest = BiharDistricts.findNearest(lat, lon)
        selectDistrict(nearest)
    }

    fun clearCache() {
        viewModelScope.launch {
            repository.clearCache()
            loadWeatherForDistrict(_selectedDistrict.value, forceRefresh = true)
            loadAllDistricts(forceRefresh = true)
        }
    }
}
