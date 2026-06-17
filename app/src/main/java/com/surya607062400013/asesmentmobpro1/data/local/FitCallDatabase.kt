package com.surya607062400013.asesmentmobpro1.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.surya607062400013.asesmentmobpro1.data.local.dao.HistoryDao
import com.surya607062400013.asesmentmobpro1.data.local.dao.MealDao
import com.surya607062400013.asesmentmobpro1.data.local.entity.HistoryEntity
import com.surya607062400013.asesmentmobpro1.data.local.entity.MealEntity

@Database(
    entities = [HistoryEntity::class, MealEntity::class],
    version = 5,
    exportSchema = false
)
abstract class FitCallDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun mealDao(): MealDao
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

        //Migrasi dari versi 3 ke 4
        private val MIGRATION_3_4 = object : androidx.room.migration.Migration(3, 4) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `meals` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`serverId` INTEGER, " +
                            "`googleId` TEXT NOT NULL, " +
                            "`name` TEXT NOT NULL, " +
                            "`description` TEXT NOT NULL, " +
                            "`imageUrl` TEXT, " +
                            "`localImagePath` TEXT, " +
                            "`calories` INTEGER NOT NULL, " +
                            "`createdAt` INTEGER NOT NULL, " +
                            "`isSynced` INTEGER NOT NULL, " +
                            "`pendingAction` TEXT)"
                )
            }
        }

        //Migrasi dari versi 4 ke 5
        private val MIGRATION_4_5 = object : androidx.room.migration.Migration(4, 5) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE history ADD COLUMN googleId TEXT NOT NULL DEFAULT ''"
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
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}