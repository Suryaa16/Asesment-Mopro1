package com.surya607062400013.asesmentmobpro1.data.remote

import com.surya607062400013.asesmentmobpro1.data.remote.dto.MealDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {
    
    // Ambil data berdasarkan google_id
    @GET("rest/v1/meals?select=*")
    suspend fun getMeals(
        @Query("google_id") googleId: String
    ): Response<List<MealDto>>

    // Tambah data
    @POST("rest/v1/meals")
    suspend fun createMeal(
        @retrofit2.http.Header("Prefer") prefer: String = "return=representation",
        @Body meal: MealDto
    ): Response<List<MealDto>>

    // Update data
    @retrofit2.http.PATCH("rest/v1/meals")
    suspend fun updateMeal(
        @Query("id") eqId: String,
        @retrofit2.http.Header("Prefer") prefer: String = "return=representation",
        @Body meal: MealDto
    ): Response<List<MealDto>>

    // Hapus data
    @DELETE("rest/v1/meals")
    suspend fun deleteMeal(
        @Query("id") eqId: String
    ): Response<Unit>

    // Upload Gambar ke Supabase Storage
    @retrofit2.http.Multipart
    @POST("storage/v1/object/meal_images/{fileName}")
    suspend fun uploadImage(
        @retrofit2.http.Path("fileName") fileName: String,
        @retrofit2.http.Part file: okhttp3.MultipartBody.Part
    ): Response<okhttp3.ResponseBody>
}
