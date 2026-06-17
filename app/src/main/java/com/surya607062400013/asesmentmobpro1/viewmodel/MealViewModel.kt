package com.surya607062400013.asesmentmobpro1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.surya607062400013.asesmentmobpro1.FitcallApplication
import com.surya607062400013.asesmentmobpro1.data.local.entity.MealEntity
import com.surya607062400013.asesmentmobpro1.data.repository.MealRepository
import com.surya607062400013.asesmentmobpro1.data.datastore.SettingsDataStore
import com.surya607062400013.asesmentmobpro1.utils.NetworkMonitor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.surya607062400013.asesmentmobpro1.R

@OptIn(ExperimentalCoroutinesApi::class)
class MealViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MealRepository = (application as FitcallApplication).mealRepository
    private val settingsDataStore = SettingsDataStore(application)
    private val networkMonitor = NetworkMonitor(application)

    val isNetworkAvailable: StateFlow<Boolean> = networkMonitor.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _hasBeenOnline = MutableStateFlow(false)
    val hasBeenOnline: StateFlow<Boolean> = _hasBeenOnline.asStateFlow()

    val googleId: StateFlow<String> = settingsDataStore.googleId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val meals: StateFlow<List<MealEntity>> = googleId
        .flatMapLatest { id ->
            if (id.isEmpty()) flowOf(emptyList())
            else repository.getMeals(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            isNetworkAvailable.collectLatest { isConnected ->
                if (isConnected) {
                    _hasBeenOnline.value = true
                    val id = googleId.value
                    if (id.isNotEmpty()) {
                        _isLoading.value = true
                        repository.syncPendingMeals()
                        repository.refreshMealsFromServer(id)
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    fun addMeal(meal: MealEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.addMeal(meal)
            if (result.isFailure) {
                _errorMessage.value = getApplication<Application>().getString(R.string.error_add_meal_offline, result.exceptionOrNull()?.message ?: "")
            }
            _isLoading.value = false
        }
    }

    fun updateMeal(meal: MealEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.updateMeal(meal)
            if (result.isFailure) {
                _errorMessage.value = getApplication<Application>().getString(R.string.error_update_meal_offline)
            }
            _isLoading.value = false
        }
    }

    fun deleteMeal(meal: MealEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.deleteMeal(meal)
            if (result.isFailure) {
                _errorMessage.value = getApplication<Application>().getString(R.string.error_delete_meal_offline)
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
