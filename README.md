# Bihar Mausam (बिहार मौसम) 🌦️

A native Android weather application for exploring current conditions, hourly forecasts, 7-day outlooks, and district-level weather insights across all **38 districts of Bihar, India**. Built with **Kotlin**, **Jetpack Compose**, **Material Design 3**, and the free **Open-Meteo API**.

> **Project highlights:** bilingual English/Hindi district browsing, interactive 24-hour temperature chart, offline cache, optional location detection, and statewide weather analytics.

## 📱 Screenshots

Screenshots of the application are included below to demonstrate the interface and its main features.

<img width="500" height="500" alt="telegram-cloud-photo-size-5-6226256928027907208-w" src="https://github.com/user-attachments/assets/21a15ea8-13e9-4b26-af91-c098e651de90" />
<img width="500" height="500" alt="telegram-cloud-photo-size-5-6226256928027907211-w" src="https://github.com/user-attachments/assets/4b8f0817-c4c6-45a4-a303-9e01e0e6deae" />
<img width="500" height="500" alt="telegram-cloud-photo-size-5-6226256928027907212-w" src="https://github.com/user-attachments/assets/6a046184-9fe4-4c2b-86e2-7298d9f7f047" />
<img width="500" height="500" alt="telegram-cloud-photo-size-5-6226256928027907210-w" src="https://github.com/user-attachments/assets/630703e1-74e6-4ceb-b3d8-1225e201ceef" />
<img width="500" height="500" alt="telegram-cloud-photo-size-5-6226256928027907209-w" src="https://github.com/user-attachments/assets/cbe4685e-ca09-4ec5-b020-d1a30bdbf970" />


## 📦 Download APK

A prebuilt Android APK is attached to this GitHub repository for users who want to install the app without building the project from source.

1. Open the repository on GitHub.
2. Locate the APK file committed to the repository (for example, `Bihar-Mausam.apk`) or open the repository's **Releases** section if the APK is published there.
3. Download the APK to your Android device.
4. Open the downloaded file and follow Android's installation prompts. Depending on your device and browser, you may need to allow installation from that source.

> **Security note:** Install APKs only from a repository and release you trust. For better distribution and version management, publishing the APK under GitHub **Releases** is recommended. GitHub's regular file preview is not an installer; download the APK first.

**APK location:** `Bihar-Mausam.apk` *.*

## ✨ Features

### 1. All 38 districts of Bihar
- Centralized district dataset with administrative-headquarters coordinates.
- District names shown in English and Hindi, including Patna (पटना), Gaya (गया), and Muzaffarpur (मुज़फ़्फ़रपुर).
- Browse districts by administrative division: Patna, Tirhut, Saran, Darbhanga, Kosi, Purnia, Bhagalpur, Munger, and Magadh.
- North Bihar and South Bihar regional grouping.

### 2. Weather data from Open-Meteo
- Uses the Open-Meteo Forecast API without requiring an API key for standard use.
- Requests metric weather data and uses the `Asia/Kolkata` timezone.
- Weather details include:
  - Current temperature and apparent/feels-like temperature
  - Daily high and low
  - Relative humidity
  - Wind speed and direction
  - UV index
  - Precipitation probability and rainfall
  - Cloud cover
  - Sunrise and sunset
- WMO weather codes are mapped to readable weather conditions and icons.

### 3. Hourly and daily forecasts
- 24-hour hourly forecast.
- 7-day forecast overview.
- Native Jetpack Compose temperature chart with a smooth cubic Bézier curve.
- Gradient fill under the temperature curve.
- Interactive hourly point selection/scrubbing.
- Hourly precipitation-probability bars.

### 4. Offline access and caching
- Room database stores the latest retrieved weather payloads locally.
- Cache-first display strategy helps the app remain useful during intermittent or unavailable connectivity.
- Cached weather can be viewed offline when a previous response is available.
- Live-versus-cached status and timestamp help users understand the freshness of the displayed data.
- Manual cache management is available through app preferences.

> Offline mode can show previously cached information; it does not generate new forecasts without a network connection.

### 5. Statewide weather analytics
- Average temperature across the districts represented by available data.
- Hottest and coolest district indicators.
- Rainiest and windiest district indicators.
- North Bihar versus South Bihar comparison.
- Analytics are calculated from the app's available district forecast data.

> **Interpretation:** These are app-calculated summaries of forecast/model data, not official statewide observations or government meteorological bulletins. Coverage and model estimates may affect comparisons.

### 6. Search, filters, and sorting
- Search district names in English or Devanagari Hindi.
- Filter districts by weather category, including rainy, clear, cloudy, and hot (above 35°C).
- Sort by district name, temperature, or rainfall.
- Optional GPS-based location lookup selects the nearest supported Bihar district when location access is available.
- Manual district selection remains available if location permission is denied or GPS cannot be used.

### 7. Preferences
- Temperature units: Celsius (°C) or Fahrenheit (°F).
- Wind speed units: km/h or m/s.
- Choose a default district.
- Manage locally cached weather data.

### 8. Bilingual experience
- District names are presented in English and Hindi.
- Search supports both Latin-script and Devanagari queries.

## 🧰 Technology stack

| Area | Technology |
|---|---|
| Platform | Native Android |
| Language | Kotlin |
| Minimum SDK | API 24 (Android 7.0) |
| Target SDK | API 36 |
| UI | Jetpack Compose, Material Design 3 |
| Architecture | MVVM + Repository Pattern |
| Networking | Retrofit 2.12, OkHttp 4.10 |
| JSON serialization | Moshi 1.15, Kotlin reflection / KSP code generation |
| Local database | Room 2.7, SQLite |
| Async and state | Kotlin Coroutines, `StateFlow`, `collectAsStateWithLifecycle` |
| Tests | JUnit 4, Robolectric |
| Weather provider | Open-Meteo Forecast API |

**Runtime note:** The project build configuration and installed Android SDK determine the exact supported build environment. The declared minimum SDK is API 24, while the run instructions below refer to Android devices/emulators compatible with the app configuration.

## 🏛️ Architecture overview

The app follows **MVVM** with a **Repository Pattern**, separating presentation, business/data coordination, remote networking, and local persistence.

```text
Jetpack Compose UI
       |
       v
 ViewModel / StateFlow
       |
       v
   Repository
    /       \
   v         v
Open-Meteo   Room Database
 Retrofit    Local cache
   \         /
    v       v
 Weather domain/data models
```

- **UI layer:** Compose screens render state and send user actions to ViewModels.
- **ViewModel layer:** exposes screen state through `StateFlow` and coordinates user actions.
- **Repository layer:** centralizes data access and chooses between remote responses and cached data.
- **Remote data source:** Retrofit/OkHttp requests forecasts from Open-Meteo.
- **Local data source:** Room stores weather responses for offline fallback.
- **Coroutine-based operations:** network and database work run asynchronously to keep the UI responsive.

The diagram is a conceptual overview; refer to the source code for the concrete package structure and implementation details.

## 🌐 API and data attribution

### Open-Meteo
Weather forecasts are supplied by [Open-Meteo](https://open-meteo.com/), using its [Forecast API](https://open-meteo.com/en/docs).

- Standard forecast access does not require an API key.
- Weather data is provided by numerical weather models and may differ from conditions measured at a particular location.
- Review Open-Meteo's current [terms](https://open-meteo.com/en/terms) and attribution requirements before distributing the app, especially if your usage or distribution becomes commercial.
- Where applicable, attribute Open-Meteo and its data sources under the relevant **CC BY 4.0** terms. Check the provider's current licensing information rather than assuming every dataset or service has identical terms.

### Official alerts and safety
This application is an informational forecast tool. It is **not an official warning system** and must not be used as the sole source for emergency decisions. Official severe-weather warnings, heat-wave advisories, flood alerts, and cyclone information should be checked with the [India Meteorological Department (IMD)](https://mausam.imd.gov.in/) and the [Bihar State Disaster Management Authority (BSDMA)](https://bsdma.org/), where available.

## 🚀 Getting started

### Prerequisites
- Android Studio (a recent stable version such as Ladybug, Meerkat, or newer).
- JDK 17 or JDK 21, as supported by the project's Gradle/Android Gradle Plugin configuration.
- Android SDK Platform 36 and the required SDK build tools.
- Internet access for the first dependency sync and live weather requests.

### Clone the repository

```bash
git clone https://github.com/<YOUR-USERNAME>/<YOUR-REPOSITORY>.git
cd <YOUR-REPOSITORY>
```

Replace the placeholders with the actual GitHub owner and repository name.

### Open and build
1. Open the cloned project folder in Android Studio.
2. Allow Gradle to sync and download dependencies.
3. Connect an Android device or start an emulator.
4. Run the `app` configuration from Android Studio.

Build a debug APK from the project root:

```bash
./gradlew assembleDebug
```

On Windows:

```bat
gradlew.bat assembleDebug
```

The generated APK is typically located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

If the repository uses a different Gradle wrapper or module name, adjust the command accordingly. Prefer `./gradlew` over a globally installed `gradle` command when the wrapper is checked into the repository.

### Run unit tests

```bash
./gradlew :app:testDebugUnitTest
```

To build and test from Android Studio, use the Gradle tool window or the Run/Debug controls.

### Install the APK using ADB (optional)

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

For a manually downloaded APK, open it on the Android device and follow the installation prompts. Only install APKs from sources you trust.

## 🔐 Permissions and privacy
- **Internet:** required to retrieve current weather and forecasts.
- **Location (optional):** used to identify the nearest supported Bihar district when the user enables location-based selection.
- The app should continue to support manual district selection when location permission is unavailable.
- Review the Android manifest and source code for the exact permissions, data handling, and privacy behavior implemented by the current build.

Do not add API keys, signing keys, passwords, or other secrets to the repository. The standard Open-Meteo forecast endpoint does not require an API key for typical non-commercial use; verify current provider terms for your intended use.

## 🗂️ Suggested repository structure

```text
.
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/                 # Kotlin source code
│   │   │   ├── res/                  # Android resources
│   │   │   └── AndroidManifest.xml
│   │   └── test/                     # Unit tests
│   └── build.gradle.kts
├── screenshots/
│   ├── home.png
│   ├── hourly.png
│   ├── forecast-7-day.png
│   ├── districts.png
│   ├── analytics.png
│   └── settings.png
├── Bihar-Mausam.apk                  # Example: use your actual APK filename
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

This is a suggested layout, not a claim about the exact current contents of the repository. Update it to reflect your actual files.

## 🧪 Testing

The project is configured to use JUnit 4 and Robolectric for unit tests. Run:

```bash
./gradlew :app:testDebugUnitTest
```

Test coverage should include, where implemented:
- WMO weather-code mapping.
- Unit conversion (°C/°F and km/h/m/s).
- District search, filtering, sorting, and division grouping.
- Analytics calculations and missing-data handling.
- Repository behavior for successful network responses, failures, and cached fallback.
- ViewModel state transitions and error/loading states.

The list above is a recommended test checklist, not a claim that every item already has a test.

## 🛠️ Troubleshooting

- **Gradle sync fails:** verify the configured JDK, Android Gradle Plugin version, Gradle wrapper, and installed SDK components.
- **Weather is unavailable:** check internet access and Open-Meteo service availability; previously cached data may still be shown.
- **No nearby district is selected:** check location permission and device location settings, or select a district manually.
- **Screenshots do not display on GitHub:** confirm that each image exists at the exact case-sensitive path referenced in this README.
- **APK cannot be installed:** download the complete `.apk` file, verify that it is compatible with the device's Android version and CPU architecture, and check whether a conflicting app signature or package version is installed.

## 🗺️ Roadmap

Potential improvements for future versions:
- Automated refresh and clearer forecast freshness indicators.
- Expanded district comparison and historical trend visualizations.
- Improved accessibility, UI testing, and localization.
- Release notes and versioned APK downloads through GitHub Releases.
- Additional official alert links and guidance, while clearly distinguishing official alerts from model forecasts.

## 🤝 Contributing

Contributions, bug reports, and suggestions are welcome.

1. Fork the repository.
2. Create a feature branch.
3. Make your changes and add or update tests where appropriate.
4. Verify the build and relevant tests.
5. Open a pull request describing the change.

## 🙌 Acknowledgements

- [Open-Meteo](https://open-meteo.com/) for weather forecast data.
- Android, Kotlin, Jetpack Compose, Material Design 3, Retrofit, OkHttp, Moshi, Room, Coroutines, JUnit, and Robolectric communities for the tools used by the project.

---

**Bihar Mausam (बिहार मौसम)** — weather information for Bihar, district by district. 🌦️
