package com.surya607062400013.asesmentmobpro1.data.repository

import com.surya607062400013.asesmentmobpro1.data.local.dao.HistoryDao
import com.surya607062400013.asesmentmobpro1.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val dao: HistoryDao) {
    fun getAllHistory(): Flow<List<HistoryEntity>> = dao.getAllHistory()
    fun getHistoryType(type: String): Flow<List<HistoryEntity>> = dao.getHistoryByType(type)
    fun getRecycleBin(): Flow<List<HistoryEntity>> = dao.getRecycleBin()

    suspend fun getById(id: Int): HistoryEntity? = dao.getById(id)
    suspend fun insert(history: HistoryEntity) = dao.insert(history)
    suspend fun update(history: HistoryEntity) = dao.update(history)
    suspend fun softDelete(id: Int) = dao.softDelete(id)
    suspend fun restore(id: Int) = dao.restore(id)
    suspend fun deletePermanent(id: Int) = dao.deletePermanent(id)
}