package com.surya607062400013.asesmentmobpro1.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.surya607062400013.asesmentmobpro1.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface HistoryDao {
    //Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: HistoryEntity)

    //Get semua history yang belum dihapus
    @Query("SELECT * FROM history WHERE isDeleted = 0 ORDER BY date DESC")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    //Get history berdasarkan type
    @Query("SELECT * FROM history WHERE isDeleted = 0 AND type = :type ORDER BY date DESC")
    fun getHistoryByType(type: String): Flow<List<HistoryEntity>>

    //Get recycle bin
    @Query("SELECT * FROM history WHERE isDeleted = 1 ORDER BY date DESC")
    fun getRecycleBin(): Flow<List<HistoryEntity>>

    //Get by id untuk detail atau edit
    @Query("SELECT * FROM history WHERE id = :id")
    suspend fun getById(id: Int): HistoryEntity?

    //Soft delete untuk pindah ke recycle bin
    @Query("UPDATE history SET isDeleted = 1 WHERE id = :id")
    suspend fun softDelete(id: Int)

    //Restore dari recycle bin
    @Query("UPDATE history SET isDeleted = 0 WHERE id = :id")
    suspend fun restore(id: Int)

    //Hapus permanen
    @Query("DELETE FROM history WHERE id = :id")
    suspend fun deletePermanent(id: Int)

    //Update
    @Update
    suspend fun update(history: HistoryEntity)
}