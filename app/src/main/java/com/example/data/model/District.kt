package com.example.data.model

data class District(
    val id: String,
    val nameEn: String,
    val nameHi: String,
    val hqEn: String,
    val latitude: Double,
    val longitude: Double,
    val division: String,
    val zone: String // "North Bihar" or "South Bihar"
) {
    val displayName: String
        get() = "$nameEn ($nameHi)"
}
