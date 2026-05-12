package com.surya607062400013.asesmentmobpro1.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FitCalcViewModel : ViewModel() {

    private val _bmiResult = MutableStateFlow<Double?>(null)
    val bmiResult: StateFlow<Double?> = _bmiResult.asStateFlow()

    private val _bmiCategory = MutableStateFlow("")
    val bmiCategory: StateFlow<String> = _bmiCategory.asStateFlow()

    fun calculateBmi(weight: Double, height: Double, isMetric: Boolean) {
        val bmi = if (isMetric) {
            val hMeters = height / 100.0
            weight / (hMeters * hMeters)
        } else {
            703 * weight / (height * height)
        }
        _bmiResult.value = bmi
        _bmiCategory.value = when {
            bmi < 18.5 -> "Underweight"
            bmi < 25.0 -> "Normal"
            bmi < 30.0 -> "Overweight"
            else -> "Obese"
        }
    }

    fun resetBmi() {
        _bmiResult.value = null
        _bmiCategory.value = ""
    }

    //Calorie State
    private val _calorieResult = MutableStateFlow<Double?>(null)
    val calorieResult: StateFlow<Double?> = _calorieResult.asStateFlow()

    fun calculateCalorie(
        age: Int,
        weight: Double,
        height: Double,
        isMale: Boolean,
        activityMultiplier: Double
    ) {
        val bmr = if (isMale) {
            88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
        } else {
            447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
        }
        _calorieResult.value = bmr * activityMultiplier
    }

    fun resetCalorie() {
        _calorieResult.value = null
    }

    // ── Protein State ──
    private val _proteinMin = MutableStateFlow<Double?>(null)
    val proteinMin: StateFlow<Double?> = _proteinMin.asStateFlow()

    private val _proteinMax = MutableStateFlow<Double?>(null)
    val proteinMax: StateFlow<Double?> = _proteinMax.asStateFlow()

    fun calculateProtein(weight: Double, multiplierMin: Double, multiplierMax: Double) {
        _proteinMin.value = weight * multiplierMin
        _proteinMax.value = weight * multiplierMax
    }

    fun resetProtein() {
        _proteinMin.value = null
        _proteinMax.value = null
    }
}