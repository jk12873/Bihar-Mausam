package com.example.data.model

import kotlin.math.*

object BiharDistricts {

    val DEFAULT_DISTRICT_ID = "patna"

    val list: List<District> = listOf(
        District(
            id = "araria",
            nameEn = "Araria",
            nameHi = "अररिया",
            hqEn = "Araria",
            latitude = 26.1498,
            longitude = 87.5186,
            division = "Purnia",
            zone = "North Bihar"
        ),
        District(
            id = "arwal",
            nameEn = "Arwal",
            nameHi = "अरवल",
            hqEn = "Arwal",
            latitude = 25.2440,
            longitude = 84.6708,
            division = "Magadh",
            zone = "South Bihar"
        ),
        District(
            id = "aurangabad",
            nameEn = "Aurangabad",
            nameHi = "औरंगाबाद",
            hqEn = "Aurangabad",
            latitude = 24.7539,
            longitude = 84.3742,
            division = "Magadh",
            zone = "South Bihar"
        ),
        District(
            id = "banka",
            nameEn = "Banka",
            nameHi = "बांका",
            hqEn = "Banka",
            latitude = 24.8887,
            longitude = 86.9242,
            division = "Bhagalpur",
            zone = "South Bihar"
        ),
        District(
            id = "begusarai",
            nameEn = "Begusarai",
            nameHi = "बेगूसराय",
            hqEn = "Begusarai",
            latitude = 25.4182,
            longitude = 86.1272,
            division = "Munger",
            zone = "North Bihar"
        ),
        District(
            id = "bhagalpur",
            nameEn = "Bhagalpur",
            nameHi = "भागलपुर",
            hqEn = "Bhagalpur",
            latitude = 25.2425,
            longitude = 86.9842,
            division = "Bhagalpur",
            zone = "South Bihar"
        ),
        District(
            id = "bhojpur",
            nameEn = "Bhojpur",
            nameHi = "भोजपुर",
            hqEn = "Arrah",
            latitude = 25.5541,
            longitude = 84.6661,
            division = "Patna",
            zone = "South Bihar"
        ),
        District(
            id = "buxar",
            nameEn = "Buxar",
            nameHi = "बक्सर",
            hqEn = "Buxar",
            latitude = 25.5647,
            longitude = 83.9777,
            division = "Patna",
            zone = "South Bihar"
        ),
        District(
            id = "darbhanga",
            nameEn = "Darbhanga",
            nameHi = "दरभंगा",
            hqEn = "Darbhanga",
            latitude = 26.1542,
            longitude = 85.8918,
            division = "Darbhanga",
            zone = "North Bihar"
        ),
        District(
            id = "east_champaran",
            nameEn = "East Champaran",
            nameHi = "पूर्वी चंपारण",
            hqEn = "Motihari",
            latitude = 26.6470,
            longitude = 84.9089,
            division = "Tirhut",
            zone = "North Bihar"
        ),
        District(
            id = "gaya",
            nameEn = "Gaya",
            nameHi = "गया",
            hqEn = "Gaya",
            latitude = 24.7914,
            longitude = 85.0002,
            division = "Magadh",
            zone = "South Bihar"
        ),
        District(
            id = "gopalganj",
            nameEn = "Gopalganj",
            nameHi = "गोपालगंज",
            hqEn = "Gopalganj",
            latitude = 26.4674,
            longitude = 84.4441,
            division = "Saran",
            zone = "North Bihar"
        ),
        District(
            id = "jamui",
            nameEn = "Jamui",
            nameHi = "जमुई",
            hqEn = "Jamui",
            latitude = 24.9270,
            longitude = 86.2230,
            division = "Munger",
            zone = "South Bihar"
        ),
        District(
            id = "jehanabad",
            nameEn = "Jehanabad",
            nameHi = "जहानाबाद",
            hqEn = "Jehanabad",
            latitude = 25.2133,
            longitude = 84.9870,
            division = "Magadh",
            zone = "South Bihar"
        ),
        District(
            id = "kaimur",
            nameEn = "Kaimur",
            nameHi = "कैमूर",
            hqEn = "Bhabua",
            latitude = 25.0425,
            longitude = 83.6144,
            division = "Patna",
            zone = "South Bihar"
        ),
        District(
            id = "katihar",
            nameEn = "Katihar",
            nameHi = "कटिहार",
            hqEn = "Katihar",
            latitude = 25.5394,
            longitude = 87.5719,
            division = "Purnia",
            zone = "North Bihar"
        ),
        District(
            id = "khagaria",
            nameEn = "Khagaria",
            nameHi = "खगड़िया",
            hqEn = "Khagaria",
            latitude = 25.5036,
            longitude = 86.4831,
            division = "Munger",
            zone = "North Bihar"
        ),
        District(
            id = "kishanganj",
            nameEn = "Kishanganj",
            nameHi = "किशनगंज",
            hqEn = "Kishanganj",
            latitude = 26.0968,
            longitude = 87.9435,
            division = "Purnia",
            zone = "North Bihar"
        ),
        District(
            id = "lakhisarai",
            nameEn = "Lakhisarai",
            nameHi = "लखीसराय",
            hqEn = "Lakhisarai",
            latitude = 25.1764,
            longitude = 86.0945,
            division = "Munger",
            zone = "South Bihar"
        ),
        District(
            id = "madhepura",
            nameEn = "Madhepura",
            nameHi = "मधेपुरा",
            hqEn = "Madhepura",
            latitude = 25.9264,
            longitude = 86.7906,
            division = "Kosi",
            zone = "North Bihar"
        ),
        District(
            id = "madhubani",
            nameEn = "Madhubani",
            nameHi = "मधुबनी",
            hqEn = "Madhubani",
            latitude = 26.3544,
            longitude = 86.0719,
            division = "Darbhanga",
            zone = "North Bihar"
        ),
        District(
            id = "munger",
            nameEn = "Munger",
            nameHi = "मुंगेर",
            hqEn = "Munger",
            latitude = 25.3757,
            longitude = 86.4744,
            division = "Munger",
            zone = "South Bihar"
        ),
        District(
            id = "muzaffarpur",
            nameEn = "Muzaffarpur",
            nameHi = "मुज़फ़्फ़रपुर",
            hqEn = "Muzaffarpur",
            latitude = 26.1209,
            longitude = 85.3647,
            division = "Tirhut",
            zone = "North Bihar"
        ),
        District(
            id = "nalanda",
            nameEn = "Nalanda",
            nameHi = "नालंदा",
            hqEn = "Bihar Sharif",
            latitude = 25.1982,
            longitude = 85.5149,
            division = "Patna",
            zone = "South Bihar"
        ),
        District(
            id = "nawada",
            nameEn = "Nawada",
            nameHi = "नवादा",
            hqEn = "Nawada",
            latitude = 24.8872,
            longitude = 85.5414,
            division = "Magadh",
            zone = "South Bihar"
        ),
        District(
            id = "patna",
            nameEn = "Patna",
            nameHi = "पटना",
            hqEn = "Patna",
            latitude = 25.5941,
            longitude = 85.1376,
            division = "Patna",
            zone = "South Bihar"
        ),
        District(
            id = "purnia",
            nameEn = "Purnia",
            nameHi = "पूर्णिया",
            hqEn = "Purnia",
            latitude = 25.7771,
            longitude = 87.4753,
            division = "Purnia",
            zone = "North Bihar"
        ),
        District(
            id = "rohtas",
            nameEn = "Rohtas",
            nameHi = "रोहतास",
            hqEn = "Sasaram",
            latitude = 24.9519,
            longitude = 84.0315,
            division = "Patna",
            zone = "South Bihar"
        ),
        District(
            id = "saharsa",
            nameEn = "Saharsa",
            nameHi = "सहरसा",
            hqEn = "Saharsa",
            latitude = 25.8835,
            longitude = 86.6006,
            division = "Kosi",
            zone = "North Bihar"
        ),
        District(
            id = "samastipur",
            nameEn = "Samastipur",
            nameHi = "समस्तीपुर",
            hqEn = "Samastipur",
            latitude = 25.8629,
            longitude = 85.7811,
            division = "Darbhanga",
            zone = "North Bihar"
        ),
        District(
            id = "saran",
            nameEn = "Saran",
            nameHi = "सारण",
            hqEn = "Chhapra",
            latitude = 25.7796,
            longitude = 84.7499,
            division = "Saran",
            zone = "North Bihar"
        ),
        District(
            id = "sheikhpura",
            nameEn = "Sheikhpura",
            nameHi = "शेखपुरा",
            hqEn = "Sheikhpura",
            latitude = 25.1378,
            longitude = 85.8611,
            division = "Munger",
            zone = "South Bihar"
        ),
        District(
            id = "sheohar",
            nameEn = "Sheohar",
            nameHi = "शिवहर",
            hqEn = "Sheohar",
            latitude = 26.5167,
            longitude = 85.2952,
            division = "Tirhut",
            zone = "North Bihar"
        ),
        District(
            id = "sitamarhi",
            nameEn = "Sitamarhi",
            nameHi = "सीतामढ़ी",
            hqEn = "Sitamarhi",
            latitude = 26.5937,
            longitude = 85.4895,
            division = "Tirhut",
            zone = "North Bihar"
        ),
        District(
            id = "siwan",
            nameEn = "Siwan",
            nameHi = "सीवान",
            hqEn = "Siwan",
            latitude = 26.2243,
            longitude = 84.3596,
            division = "Saran",
            zone = "North Bihar"
        ),
        District(
            id = "supaul",
            nameEn = "Supaul",
            nameHi = "सुपौल",
            hqEn = "Supaul",
            latitude = 26.1260,
            longitude = 86.6053,
            division = "Kosi",
            zone = "North Bihar"
        ),
        District(
            id = "vaishali",
            nameEn = "Vaishali",
            nameHi = "वैशाली",
            hqEn = "Hajipur",
            latitude = 25.6858,
            longitude = 85.2155,
            division = "Tirhut",
            zone = "North Bihar"
        ),
        District(
            id = "west_champaran",
            nameEn = "West Champaran",
            nameHi = "पश्चिम चंपारण",
            hqEn = "Bettiah",
            latitude = 26.8024,
            longitude = 84.5020,
            division = "Tirhut",
            zone = "North Bihar"
        )
    )

    fun getDistrict(id: String): District {
        return list.find { it.id.equals(id, ignoreCase = true) } ?: list.first { it.id == DEFAULT_DISTRICT_ID }
    }

    /**
     * Calculates distance between coordinates in kilometers using the Haversine formula
     */
    fun findNearest(latitude: Double, longitude: Double): District {
        var minDistance = Double.MAX_VALUE
        var nearestDistrict = getDistrict(DEFAULT_DISTRICT_ID)

        val r = 6371.0 // Earth radius in km
        val userLatRad = Math.toRadians(latitude)
        val userLonRad = Math.toRadians(longitude)

        for (d in list) {
            val dLatRad = Math.toRadians(d.latitude)
            val dLonRad = Math.toRadians(d.longitude)
            val deltaLat = dLatRad - userLatRad
            val deltaLon = dLonRad - userLonRad

            val a = sin(deltaLat / 2).pow(2) +
                    cos(userLatRad) * cos(dLatRad) * sin(deltaLon / 2).pow(2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            val distance = r * c

            if (distance < minDistance) {
                minDistance = distance
                nearestDistrict = d
            }
        }
        return nearestDistrict
    }
}
