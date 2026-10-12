package com.example

import com.example.data.model.BiharDistricts
import org.junit.Assert.*
import org.junit.Test

class DistrictDatasetTest {

    @Test
    fun testAll38DistrictsPresent() {
        assertEquals("Bihar must have exactly 38 districts", 38, BiharDistricts.list.size)
    }

    @Test
    fun testDistrictCoordinatesWithinBiharBounds() {
        // Bihar roughly spans Latitude: 24.2° N to 27.5° N, Longitude: 83.3° E to 88.3° E
        for (district in BiharDistricts.list) {
            assertTrue(
                "${district.nameEn} latitude ${district.latitude} out of bounds",
                district.latitude in 24.0..28.0
            )
            assertTrue(
                "${district.nameEn} longitude ${district.longitude} out of bounds",
                district.longitude in 83.0..89.0
            )
            assertTrue("District ID must not be blank", district.id.isNotBlank())
            assertTrue("District English name must not be blank", district.nameEn.isNotBlank())
            assertTrue("District Hindi name must not be blank", district.nameHi.isNotBlank())
        }
    }

    @Test
    fun testUniqueDistrictIdsAndNames() {
        val ids = BiharDistricts.list.map { it.id }
        assertEquals("All district IDs must be unique", ids.size, ids.toSet().size)

        val names = BiharDistricts.list.map { it.nameEn }
        assertEquals("All district English names must be unique", names.size, names.toSet().size)
    }

    @Test
    fun testFindNearestDistrict() {
        // Patna coordinates: 25.5941, 85.1376
        val nearestToPatna = BiharDistricts.findNearest(25.5941, 85.1376)
        assertEquals("patna", nearestToPatna.id)

        // Gaya coordinates: 24.7914, 85.0002
        val nearestToGaya = BiharDistricts.findNearest(24.7914, 85.0002)
        assertEquals("gaya", nearestToGaya.id)
    }
}
