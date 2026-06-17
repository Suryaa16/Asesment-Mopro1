package com.surya607062400013.asesmentmobpro1.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MealDto(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "google_id") val googleId: String,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "image_url") val imageUrl: String?,
    @Json(name = "calories") val calories: Int,
    @Json(name = "created_at") val createdAt: String? = null
)
