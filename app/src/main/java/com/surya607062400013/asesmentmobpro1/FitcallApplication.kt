package com.surya607062400013.asesmentmobpro1

import android.app.Application
import com.surya607062400013.asesmentmobpro1.data.local.FitCallDatabase
import com.surya607062400013.asesmentmobpro1.data.repository.HistoryRepository

import com.surya607062400013.asesmentmobpro1.data.remote.RetrofitClient
import com.surya607062400013.asesmentmobpro1.data.repository.MealRepository

@OptIn(coil.annotation.ExperimentalCoilApi::class)
class FitcallApplication : Application() {
    val database by lazy { FitCallDatabase.getDatabase(this) }
    val repository by lazy { HistoryRepository(database.historyDao()) }
    val mealRepository by lazy { MealRepository(database.mealDao(), RetrofitClient.apiService, this) }

    override fun onCreate() {
        super.onCreate()
        val imageLoader = coil.ImageLoader.Builder(this).build()
        imageLoader.memoryCache?.clear()
        imageLoader.diskCache?.clear()
        coil.Coil.imageLoader(this).memoryCache?.clear()
        coil.Coil.imageLoader(this).diskCache?.clear()
    }
}