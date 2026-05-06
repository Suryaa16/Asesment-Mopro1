package com.surya607062400013.asesmentmobpro1

import android.app.Application
import com.surya607062400013.asesmentmobpro1.data.local.FitCallDatabase
import com.surya607062400013.asesmentmobpro1.data.repository.HistoryRepository

class FitcallApplication : Application() {
    val database by lazy { FitCallDatabase.getDatabase(this) }
    val repository by lazy { HistoryRepository(database.historyDao()) }
}