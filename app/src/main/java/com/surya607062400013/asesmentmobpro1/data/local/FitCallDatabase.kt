package com.surya607062400013.asesmentmobpro1.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.surya607062400013.asesmentmobpro1.data.local.dao.HistoryDao
import com.surya607062400013.asesmentmobpro1.data.local.entity.HistoryEntity

@Database(
    entities = [HistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FitCallDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    companion object{
        @Volatile
        private var INSTANCE: FitCallDatabase? = null

        fun getDatabase(context: Context): FitCallDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitCallDatabase::class.java,
                    "fitcall_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}