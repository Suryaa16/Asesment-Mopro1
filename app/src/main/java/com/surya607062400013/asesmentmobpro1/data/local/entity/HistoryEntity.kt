package com.surya607062400013.asesmentmobpro1.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val result: String,
    val detail: String,
    val date: Long,
    val isDeleted: Boolean = false
)