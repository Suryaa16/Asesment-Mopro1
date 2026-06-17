package com.surya607062400013.asesmentmobpro1.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.surya607062400013.asesmentmobpro1.data.local.entity.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: MealEntity): Long

    @Update
    suspend fun update(meal: MealEntity)

    @Query("DELETE FROM meals WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM meals WHERE googleId = :googleId AND (pendingAction IS NULL OR pendingAction != 'DELETE') ORDER BY createdAt DESC")
    fun getAllMealsByUser(googleId: String): Flow<List<MealEntity>>

    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getById(id: Int): MealEntity?

    @Query("SELECT * FROM meals WHERE isSynced = 0")
    suspend fun getUnsyncedMeals(): List<MealEntity>

    @Query("DELETE FROM meals WHERE googleId = :googleId AND isSynced = 1")
    suspend fun clearSyncedMeals(googleId: String)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(meals: List<MealEntity>)
}
