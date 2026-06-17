package com.surya607062400013.asesmentmobpro1.data.repository

import android.util.Log
import com.surya607062400013.asesmentmobpro1.data.local.dao.MealDao
import com.surya607062400013.asesmentmobpro1.data.local.entity.MealEntity
import com.surya607062400013.asesmentmobpro1.data.remote.ApiService
import com.surya607062400013.asesmentmobpro1.data.remote.dto.MealDto
import kotlinx.coroutines.flow.Flow
import android.content.Context
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealRepository(
    private val mealDao: MealDao,
    private val apiService: ApiService,
    private val context: Context
) {
    companion object {
        private const val TAG = "MealRepository"
    }

    private suspend fun uploadImageIfLocal(imageUrl: String?): String? {
        if (imageUrl.isNullOrEmpty()) return null
        if (!imageUrl.startsWith("content://") && !imageUrl.startsWith("file://")) return imageUrl // already a remote URL

        return try {
            val uri = imageUrl.toUri()
            val inputStream = context.contentResolver.openInputStream(uri) ?: return imageUrl
            val tempFile = withContext(Dispatchers.IO) {
                File.createTempFile("upload", ".jpg", context.cacheDir)
            }
            val outputStream = withContext(Dispatchers.IO) {
                FileOutputStream(tempFile)
            }
            inputStream.copyTo(outputStream)
            withContext(Dispatchers.IO) {
                inputStream.close()
            }
            withContext(Dispatchers.IO) {
                outputStream.close()
            }

            val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val fileName = "${UUID.randomUUID()}.jpg"
            val body = MultipartBody.Part.createFormData("file", fileName, requestFile)

            val response = apiService.uploadImage(fileName, body)
            if (response.isSuccessful) {
                "https://niroywxmkkjpvwzopvlm.supabase.co/storage/v1/object/public/meal_images/$fileName"
            } else {
                Log.e(TAG, "Failed to upload image: ${response.message()}")
                imageUrl
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to upload image", e)
            imageUrl 
        }
    }

    fun getMeals(googleId: String): Flow<List<MealEntity>> {
        return mealDao.getAllMealsByUser(googleId)
    }

    suspend fun refreshMealsFromServer(googleId: String): Result<Unit> {
        return try {
            val response = apiService.getMeals(googleId)
            if (response.isSuccessful) {
                val mealsDto = response.body() ?: emptyList()
                val newEntities = mealsDto.map { dto ->
                    MealEntity(
                        serverId = dto.id,
                        googleId = dto.googleId,
                        name = dto.name,
                        description = dto.description,
                        imageUrl = dto.imageUrl,
                        calories = dto.calories,
                        isSynced = true,
                        pendingAction = null
                    )
                }

                mealDao.clearSyncedMeals(googleId)
                mealDao.insertAll(newEntities)

                syncPendingMeals()
                
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to fetch meals: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Refresh error", e)
            Result.failure(e)
        }
    }

    suspend fun addMeal(meal: MealEntity): Result<Unit> {
        return try {
            val mealToInsert = meal.copy(isSynced = false, pendingAction = "CREATE")
            val localId = mealDao.insert(mealToInsert)
            val uploadedUrl = uploadImageIfLocal(meal.imageUrl)
            val dto = MealDto(
                googleId = meal.googleId,
                name = meal.name,
                description = meal.description,
                imageUrl = uploadedUrl,
                calories = meal.calories
            )
            
            val response = apiService.createMeal(meal = dto)
            if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                val serverMeal = response.body()!![0]
                val syncedMeal = mealToInsert.copy(
                    id = localId.toInt(),
                    serverId = serverMeal.id,
                    imageUrl = uploadedUrl,
                    isSynced = true,
                    pendingAction = null
                )
                mealDao.update(syncedMeal)
            } else {
                if (uploadedUrl != meal.imageUrl) {
                    mealDao.update(mealToInsert.copy(id = localId.toInt(), imageUrl = uploadedUrl))
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Add meal error, saved locally", e)
            Result.success(Unit)
        }
    }

    suspend fun updateMeal(meal: MealEntity): Result<Unit> {
        return try {
            val mealToUpdate = meal.copy(isSynced = false, pendingAction = "UPDATE")
            mealDao.update(mealToUpdate)

            if (mealToUpdate.serverId == null) {
                return Result.success(Unit)
            }

            val uploadedUrl = uploadImageIfLocal(meal.imageUrl)
            val dto = MealDto(
                id = null,
                googleId = mealToUpdate.googleId,
                name = mealToUpdate.name,
                description = mealToUpdate.description,
                imageUrl = uploadedUrl,
                calories = mealToUpdate.calories
            )
            
            val response = apiService.updateMeal("eq.${mealToUpdate.serverId}", meal = dto)
            if (response.isSuccessful) {
                val syncedMeal = mealToUpdate.copy(
                    imageUrl = uploadedUrl,
                    isSynced = true,
                    pendingAction = null
                )
                mealDao.update(syncedMeal)
            } else {
                if (uploadedUrl != meal.imageUrl) {
                    mealDao.update(mealToUpdate.copy(imageUrl = uploadedUrl))
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Update meal error, marked locally", e)
            Result.success(Unit)
        }
    }

    suspend fun deleteMeal(meal: MealEntity): Result<Unit> {
        return try {
            if (meal.serverId == null) {
                mealDao.deleteById(meal.id)
                Result.success(Unit)
            } else {
                val mealToDelete = meal.copy(isSynced = false, pendingAction = "DELETE")
                mealDao.update(mealToDelete)

                val response = apiService.deleteMeal("eq.${meal.serverId}")
                if (response.isSuccessful) {
                    mealDao.deleteById(meal.id)
                }
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Delete meal error, marked locally", e)
            Result.success(Unit)
        }
    }

    suspend fun syncPendingMeals() {
        try {
            val unsyncedMeals = mealDao.getUnsyncedMeals()
            for (meal in unsyncedMeals) {
                when (meal.pendingAction) {
                    "CREATE" -> {
                        val uploadedUrl = uploadImageIfLocal(meal.imageUrl)
                        val dto = MealDto(
                            googleId = meal.googleId,
                            name = meal.name,
                            description = meal.description,
                            imageUrl = uploadedUrl,
                            calories = meal.calories
                        )
                        val response = apiService.createMeal(meal = dto)
                        if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
                            val serverId = response.body()!![0].id
                            mealDao.update(meal.copy(serverId = serverId, imageUrl = uploadedUrl, isSynced = true, pendingAction = null))
                        } else if (uploadedUrl != meal.imageUrl) {
                            mealDao.update(meal.copy(imageUrl = uploadedUrl))
                        }
                    }
                    "UPDATE" -> {
                        if (meal.serverId != null) {
                            val uploadedUrl = uploadImageIfLocal(meal.imageUrl)
                            val dto = MealDto(
                                id = null,
                                googleId = meal.googleId,
                                name = meal.name,
                                description = meal.description,
                                imageUrl = uploadedUrl,
                                calories = meal.calories
                            )
                            val response = apiService.updateMeal("eq.${meal.serverId}", meal = dto)
                            if (response.isSuccessful) {
                                mealDao.update(meal.copy(imageUrl = uploadedUrl, isSynced = true, pendingAction = null))
                            } else if (uploadedUrl != meal.imageUrl) {
                                mealDao.update(meal.copy(imageUrl = uploadedUrl))
                            }
                        }
                    }
                    "DELETE" -> {
                        if (meal.serverId != null) {
                            val response = apiService.deleteMeal("eq.${meal.serverId}")
                            if (response.isSuccessful) {
                                mealDao.deleteById(meal.id)
                            }
                        } else {
                            mealDao.deleteById(meal.id)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sync error", e)
        }
    }
}
