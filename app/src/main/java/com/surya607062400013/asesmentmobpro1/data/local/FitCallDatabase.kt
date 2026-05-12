package com.surya607062400013.asesmentmobpro1.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.surya607062400013.asesmentmobpro1.data.local.dao.HistoryDao
import com.surya607062400013.asesmentmobpro1.data.local.entity.HistoryEntity

@Database(
    entities = [HistoryEntity::class],
    version = 3,
    exportSchema = false
)
abstract class FitCallDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    companion object{
        @Volatile
        private var INSTANCE: FitCallDatabase? = null

        //Migrasi dari versi 1 ke 2
        private val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE history ADD COLUMN isEdited INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        //Migrasi dari versi 2 ke 3
        private val MIGRATION_2_3 = object : androidx.room.migration.Migration(2, 3) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE history ADD COLUMN name TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        fun getDatabase(context: Context): FitCallDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitCallDatabase::class.java,
                    "fitcall_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}