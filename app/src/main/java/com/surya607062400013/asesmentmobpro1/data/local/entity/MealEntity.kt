package com.surya607062400013.asesmentmobpro1.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serverId: Int? = null,
    val googleId: String,
    val name: String,
    val description: String,
    val imageUrl: String? = null,
    val localImagePath: String? = null,
    val calories: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val pendingAction: String? = null
)
