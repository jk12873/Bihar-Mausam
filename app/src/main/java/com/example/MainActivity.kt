package com.example

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.DistrictSwitcherBottomSheet
import com.example.ui.screens.DistrictsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.StateOverviewScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.BiharMausamTheme
import com.example.ui.theme.SkyBluePrimary
import com.example.ui.viewmodel.WeatherViewModel

enum class NavigationScreen(val labelEn: String, val labelHi: String) {
    HOME("Home", "होम"),
    DISTRICTS("Districts", "ज़िले"),
    OVERVIEW("Overview", "अवलोकन"),
    SETTINGS("Settings", "सेटिंग्स")
}

class MainActivity : ComponentActivity() {

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BiharMausamTheme {
                MainAppScaffold(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScaffold(viewModel: WeatherViewModel) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf(NavigationScreen.HOME) }
    var showDistrictSheet by remember { mutableStateOf(false) }

    val selectedDistrict by viewModel.selectedDistrict.collectAsStateWithLifecycle()
    val weatherState by viewModel.currentWeatherState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val allDistricts by viewModel.allDistricts.collectAsStateWithLifecycle()
    val filteredDistricts by viewModel.filteredDistricts.collectAsStateWithLifecycle()
    val isLoadingDistricts by viewModel.isLoadingDistricts.collectAsStateWithLifecycle()
    val stateSummary by viewModel.stateSummary.collectAsStateWithLifecycle()
    val tempUnit by viewModel.tempUnit.collectAsStateWithLifecycle()
    val windUnit by viewModel.windUnit.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()

    // GPS Location Permission Launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            locateUserAndSelectDistrict(context, viewModel)
        } else {
            Toast.makeText(
                context,
                "Location permission denied. You can select any district manually from the list.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Handle back button on sub-screens to return to Home
    if (currentScreen != NavigationScreen.HOME) {
        BackHandler {
            currentScreen = NavigationScreen.HOME
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("main_app_scaffold"),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .testTag("bottom_nav_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = NavigationBarDefaults.Elevation
            ) {
                NavigationBarItem(
                    selected = currentScreen == NavigationScreen.HOME,
                    onClick = { currentScreen = NavigationScreen.HOME },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("${NavigationScreen.HOME.labelEn} • ${NavigationScreen.HOME.labelHi}") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SkyBluePrimary,
                        selectedTextColor = SkyBluePrimary,
                        indicatorColor = SkyBluePrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_home")
                )

                NavigationBarItem(
                    selected = currentScreen == NavigationScreen.DISTRICTS,
                    onClick = { currentScreen = NavigationScreen.DISTRICTS },
                    icon = { Icon(Icons.Filled.LocationCity, contentDescription = "Districts") },
                    label = { Text("${NavigationScreen.DISTRICTS.labelEn} • ${NavigationScreen.DISTRICTS.labelHi}") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SkyBluePrimary,
                        selectedTextColor = SkyBluePrimary,
                        indicatorColor = SkyBluePrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_districts")
                )

                NavigationBarItem(
                    selected = currentScreen == NavigationScreen.OVERVIEW,
                    onClick = { currentScreen = NavigationScreen.OVERVIEW },
                    icon = { Icon(Icons.Filled.Insights, contentDescription = "Overview") },
                    label = { Text("${NavigationScreen.OVERVIEW.labelEn} • ${NavigationScreen.OVERVIEW.labelHi}") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SkyBluePrimary,
                        selectedTextColor = SkyBluePrimary,
                        indicatorColor = SkyBluePrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_overview")
                )

                NavigationBarItem(
                    selected = currentScreen == NavigationScreen.SETTINGS,
                    onClick = { currentScreen = NavigationScreen.SETTINGS },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
                    label = { Text("${NavigationScreen.SETTINGS.labelEn} • ${NavigationScreen.SETTINGS.labelHi}") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SkyBluePrimary,
                        selectedTextColor = SkyBluePrimary,
                        indicatorColor = SkyBluePrimary.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_item_settings")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                NavigationScreen.HOME -> {
                    HomeScreen(
                        selectedDistrict = selectedDistrict,
                        weatherState = weatherState,
                        isRefreshing = isRefreshing,
                        tempUnit = tempUnit,
                        windUnit = windUnit,
                        onDistrictClick = { showDistrictSheet = true },
                        onRefresh = { viewModel.refreshCurrentWeather() },
                        onLocateClick = {
                            val hasPermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasPermission) {
                                locateUserAndSelectDistrict(context, viewModel)
                            } else {
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                            }
                        }
                    )
                }

                NavigationScreen.DISTRICTS -> {
                    DistrictsScreen(
                        districtsList = filteredDistricts,
                        isLoading = isLoadingDistricts,
                        searchQuery = searchQuery,
                        selectedFilter = selectedFilter,
                        sortOption = sortOption,
                        tempUnit = tempUnit,
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        onFilterChange = { viewModel.setFilter(it) },
                        onSortChange = { viewModel.setSort(it) },
                        onDistrictClick = { district ->
                            viewModel.selectDistrict(district)
                            currentScreen = NavigationScreen.HOME
                        },
                        onRefreshAll = { viewModel.loadAllDistricts(forceRefresh = true) }
                    )
                }

                NavigationScreen.OVERVIEW -> {
                    StateOverviewScreen(
                        stateSummary = stateSummary,
                        allDistricts = allDistricts,
                        isLoading = isLoadingDistricts,
                        tempUnit = tempUnit,
                        onDistrictClick = { district ->
                            viewModel.selectDistrict(district)
                            currentScreen = NavigationScreen.HOME
                        },
                        onRefresh = { viewModel.loadAllDistricts(forceRefresh = true) }
                    )
                }

                NavigationScreen.SETTINGS -> {
                    SettingsScreen(
                        tempUnit = tempUnit,
                        windUnit = windUnit,
                        selectedDistrict = selectedDistrict,
                        onToggleTempUnit = { viewModel.toggleTempUnit() },
                        onWindUnitChange = { viewModel.setWindUnit(it) },
                        onSelectDefaultDistrict = { showDistrictSheet = true },
                        onClearCache = { viewModel.clearCache() }
                    )
                }
            }
        }
    }

    if (showDistrictSheet) {
        DistrictSwitcherBottomSheet(
            selectedDistrict = selectedDistrict,
            onDistrictSelected = { district ->
                viewModel.selectDistrict(district)
            },
            onDismiss = { showDistrictSheet = false }
        )
    }
}

private fun locateUserAndSelectDistrict(context: Context, viewModel: WeatherViewModel) {
    try {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        if (locationManager == null) {
            Toast.makeText(context, "Location service not available", Toast.LENGTH_SHORT).show()
            return
        }

        var bestLocation: Location? = null
        val providers = locationManager.getProviders(true)
        for (provider in providers) {
            try {
                val loc = locationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || loc.accuracy < bestLocation.accuracy) {
                    bestLocation = loc
                }
            } catch (_: SecurityException) {
                // Ignore security exceptions for specific providers
            }
        }

        if (bestLocation != null) {
            viewModel.selectNearestDistrict(bestLocation.latitude, bestLocation.longitude)
            Toast.makeText(
                context,
                "Located nearest district based on GPS coordinates",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                "Could not determine current GPS position. Please pick your district manually.",
                Toast.LENGTH_SHORT
            ).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Location lookup failed: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
