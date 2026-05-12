package com.surya607062400013.asesmentmobpro1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.surya607062400013.asesmentmobpro1.R
import com.surya607062400013.asesmentmobpro1.data.local.entity.HistoryEntity
import com.surya607062400013.asesmentmobpro1.viewmodel.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryEditScreen(
    id: Int,
    onNavigateUp: () -> Unit,
    historyViewModel: HistoryViewModel
) {
    var item by remember { mutableStateOf<HistoryEntity?>(null) }
    var isLoaded by remember { mutableStateOf(false) }

    // State BMI
    var bmiWeight by remember { mutableStateOf("") }
    var bmiHeight by remember { mutableStateOf("") }
    var bmiWeightError by remember { mutableStateOf("") }
    var bmiHeightError by remember { mutableStateOf("") }
    var isMetric by remember { mutableStateOf(true) }

    // State Calorie
    var calAge by remember { mutableStateOf("") }
    var calWeight by remember { mutableStateOf("") }
    var calHeight by remember { mutableStateOf("") }
    var calAgeError by remember { mutableStateOf("") }
    var calWeightError by remember { mutableStateOf("") }
    var calHeightError by remember { mutableStateOf("") }
    var isMale by remember { mutableStateOf(true) }
    var selectedActivity by remember { mutableIntStateOf(0) }

    // State Protein
    var proteinWeight by remember { mutableStateOf("") }
    var proteinWeightError by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableIntStateOf(0) }

    val strFemale = stringResource(R.string.calorie_female)
    var editName by remember { mutableStateOf("") }

    // Load data existing
    LaunchedEffect(id) {
        historyViewModel.getById(id) { result ->
            item = result
            editName = result?.name ?: ""
            if (result != null) {
                val parts = result.detail.split(", ")

                // Fungsi pembantu untuk mengambil hanya angka dan titik dari string
                fun extractNumeric(text: String?): String {
                    return text?.replace(Regex("[^0-9.]"), "") ?: ""
                }

                when (result.type) {
                    "BMI" -> {
                        bmiWeight = extractNumeric(parts.getOrNull(0))
                        bmiHeight = extractNumeric(parts.getOrNull(1))
                        val unitPart = parts.getOrNull(2)?.replace("Unit: ", "") ?: "Metric"
                        isMetric = unitPart == "Metric"
                    }
                    "Calorie" -> {
                        calAge = extractNumeric(parts.getOrNull(0))
                        calWeight = extractNumeric(parts.getOrNull(1))
                        calHeight = extractNumeric(parts.getOrNull(2))
                        val genderPart = parts.getOrNull(3)?.lowercase() ?: ""
                        isMale = !genderPart.contains(strFemale.lowercase())
                    }
                    "Protein" -> {
                        proteinWeight = extractNumeric(parts.getOrNull(0))
                        // Opsional: set selectedGoal berdasarkan teks di parts.getOrNull(1)
                    }
                }
            }
            isLoaded = true
        }
    }
    //stringResource dideklarasikan di sini
    val strMetric = stringResource(R.string.bmi_metric)
    val strImperial = stringResource(R.string.bmi_imperial)
    val strWeightMetric = stringResource(R.string.bmi_weight_hint_metric)
    val strHeightMetric = stringResource(R.string.bmi_height_hint_metric)
    val strWeightImperial = stringResource(R.string.bmi_weight_hint_imperial)
    val strHeightImperial = stringResource(R.string.bmi_height_hint_imperial)
    val strUnitSystem = stringResource(R.string.bmi_metric)
    val strAge = stringResource(R.string.calorie_age)
    val strWeightKg = stringResource(R.string.calorie_weight_kg)
    val strHeightCm = stringResource(R.string.calorie_height_cm)
    val strGender = stringResource(R.string.calorie_gender)
    val strMale = stringResource(R.string.calorie_male)
    val strActivity = stringResource(R.string.calorie_activity)
    val strSedentary = stringResource(R.string.calorie_sedentary)
    val strLight = stringResource(R.string.calorie_light)
    val strModerate = stringResource(R.string.calorie_moderate)
    val strActive = stringResource(R.string.calorie_active)
    val strVeryActive = stringResource(R.string.calorie_very_active)
    val strProteinWeight = stringResource(R.string.protein_weight)
    val strGoal = stringResource(R.string.protein_goal)
    val strGoalSedentary = stringResource(R.string.protein_sedentary)
    val strGoalFitness = stringResource(R.string.protein_fitness)
    val strGoalMuscle = stringResource(R.string.protein_muscle)
    val strGoalAthlete = stringResource(R.string.protein_athlete)
    val strSave = stringResource(R.string.save)
    val strAnonymous = stringResource(R.string.anonymous)
    val strUnderweight = stringResource(R.string.bmi_underweight)
    val strNormal = stringResource(R.string.bmi_normal)
    val strOverweight = stringResource(R.string.bmi_overweight)
    val strObese = stringResource(R.string.bmi_obese)
    val strErrorEmpty = stringResource(R.string.error_empty_field)
    val strErrorNumber = stringResource(R.string.error_invalid_number)
    val strErrorWeight = stringResource(R.string.error_invalid_weight)
    val strErrorHeight = stringResource(R.string.error_invalid_height)
    val strErrorAge = stringResource(R.string.error_invalid_age)
    val strWeightLabel = stringResource(R.string.bmi_weight)
    val strHeightLabel = stringResource(R.string.bmi_height)
    val strInputName = stringResource(R.string.input_name)

    //List label pakai string dari resource
    val activityLabels = listOf(
        strSedentary, strLight, strModerate, strActive, strVeryActive
    )
    val activityMultipliers = listOf(1.2, 1.375, 1.55, 1.725, 1.9)
    val goalLabels = listOf(
        strGoalSedentary, strGoalFitness, strGoalMuscle, strGoalAthlete
    )
    val goalMultipliers = listOf(
        Pair(0.8, 1.0), Pair(1.2, 1.4), Pair(1.6, 2.0), Pair(2.0, 2.4)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (!isLoaded) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Type: ${item?.type ?: ""}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                when (item?.type) {

                    //Edit BMI
                    "BMI" -> {
                        Text(
                            text = strUnitSystem,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = isMetric,
                                onClick = { isMetric = true }
                            )
                            Text(strMetric)
                            Spacer(modifier = Modifier.width(24.dp))
                            RadioButton(
                                selected = !isMetric,
                                onClick = { isMetric = false }
                            )
                            Text(strImperial)
                        }
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text(strInputName) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = bmiWeight,
                            onValueChange = { bmiWeight = it; bmiWeightError = "" },
                            label = { Text(if (isMetric) strWeightMetric else strWeightImperial) },
                            isError = bmiWeightError.isNotEmpty(),
                            supportingText = {
                                if (bmiWeightError.isNotEmpty())
                                    Text(bmiWeightError, color = MaterialTheme.colorScheme.error)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = bmiHeight,
                            onValueChange = { bmiHeight = it; bmiHeightError = "" },
                            label = { Text(if (isMetric) strHeightMetric else strHeightImperial) },
                            isError = bmiHeightError.isNotEmpty(),
                            supportingText = {
                                if (bmiHeightError.isNotEmpty())
                                    Text(bmiHeightError, color = MaterialTheme.colorScheme.error)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                var valid = true
                                if (bmiWeight.isBlank()) {
                                    bmiWeightError = strErrorEmpty
                                    valid = false
                                } else if (bmiWeight.toDoubleOrNull() == null) {
                                    bmiWeightError = strErrorNumber
                                    valid = false
                                } else if (bmiWeight.toDouble() <= 0) {
                                    bmiWeightError = strErrorWeight
                                    valid = false
                                }
                                if (bmiHeight.isBlank()) {
                                    bmiHeightError = strErrorEmpty
                                    valid = false
                                } else if (bmiHeight.toDoubleOrNull() == null) {
                                    bmiHeightError = strErrorNumber
                                    valid = false
                                } else if (bmiHeight.toDouble() <= 0) {
                                    bmiHeightError = strErrorHeight
                                    valid = false
                                }
                                if (valid) {
                                    val w = bmiWeight.toDouble()
                                    val h = bmiHeight.toDouble()
                                    val bmi = if (isMetric) {
                                        val hMeters = h / 100.0
                                        w / (hMeters * hMeters)
                                    } else {
                                        703 * w / (h * h)
                                    }
                                    val category = when {
                                        bmi < 18.5 -> strUnderweight
                                        bmi < 25.0 -> strNormal
                                        bmi < 30.0 -> strOverweight
                                        else -> strObese
                                    }
                                    item?.let {
                                        historyViewModel.update(
                                            it.copy(
                                                name = editName.ifBlank { strAnonymous },
                                                result = "BMI: ${String.format(Locale.current.platformLocale, "%.1f", bmi)} - $category",
                                                detail = "$strWeightLabel: ${w}${if (isMetric) "kg" else "lbs"}, $strHeightLabel: ${h}${if (isMetric) "cm" else "in"}, Unit: ${if (isMetric) "Metric" else "Imperial"}",
                                                date = System.currentTimeMillis(),
                                                isEdited = true
                                            )
                                        )
                                    }
                                    onNavigateUp()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(strSave, fontSize = 16.sp)
                        }
                    }

                    //Edit Calorie
                    "Calorie" -> {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text(strInputName) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = calAge,
                            onValueChange = { calAge = it; calAgeError = "" },
                            label = { Text(strAge) },
                            isError = calAgeError.isNotEmpty(),
                            supportingText = {
                                if (calAgeError.isNotEmpty())
                                    Text(calAgeError, color = MaterialTheme.colorScheme.error)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = calWeight,
                            onValueChange = { calWeight = it; calWeightError = "" },
                            label = { Text(strWeightKg) },
                            isError = calWeightError.isNotEmpty(),
                            supportingText = {
                                if (calWeightError.isNotEmpty())
                                    Text(calWeightError, color = MaterialTheme.colorScheme.error)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = calHeight,
                            onValueChange = { calHeight = it; calHeightError = "" },
                            label = { Text(strHeightCm) },
                            isError = calHeightError.isNotEmpty(),
                            supportingText = {
                                if (calHeightError.isNotEmpty())
                                    Text(calHeightError, color = MaterialTheme.colorScheme.error)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Text(strGender, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(selected = isMale, onClick = { isMale = true })
                            Text(strMale)
                            Spacer(modifier = Modifier.width(24.dp))
                            RadioButton(selected = !isMale, onClick = { isMale = false })
                            Text(strFemale)
                        }

                        Text(strActivity, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        activityLabels.forEachIndexed { index, label ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedActivity == index,
                                    onClick = { selectedActivity = index }
                                )
                                Text(label, fontSize = 14.sp)
                            }
                        }

                        Button(
                            onClick = {
                                var valid = true
                                if (calAge.isBlank()) {
                                    calAgeError = strErrorEmpty
                                    valid = false
                                } else if (calAge.toIntOrNull() == null) {
                                    calAgeError = strErrorNumber
                                    valid = false
                                } else if (calAge.toInt() !in 1..120) {
                                    calAgeError = strErrorAge
                                    valid = false
                                }
                                if (calWeight.isBlank()) {
                                    calWeightError = strErrorEmpty
                                    valid = false
                                } else if (calWeight.toDoubleOrNull() == null) {
                                    calWeightError = strErrorNumber
                                    valid = false
                                } else if (calWeight.toDouble() <= 0) {
                                    calWeightError = strErrorWeight
                                    valid = false
                                }
                                if (calHeight.isBlank()) {
                                    calHeightError = strErrorEmpty
                                    valid = false
                                } else if (calHeight.toDoubleOrNull() == null) {
                                    calHeightError = strErrorNumber
                                    valid = false
                                } else if (calHeight.toDouble() <= 0) {
                                    calHeightError = strErrorHeight
                                    valid = false
                                }
                                if (valid) {
                                    val a = calAge.toInt()
                                    val w = calWeight.toDouble()
                                    val h = calHeight.toDouble()
                                    val bmr = if (isMale) {
                                        88.362 + (13.397 * w) + (4.799 * h) - (5.677 * a)
                                    } else {
                                        447.593 + (9.247 * w) + (3.098 * h) - (4.330 * a)
                                    }
                                    val calories = bmr * activityMultipliers[selectedActivity]
                                    item?.let {
                                        historyViewModel.update(
                                            it.copy(
                                                result = "${String.format(Locale.current.platformLocale, "%.0f", calories)} kcal/day",
                                                detail = "$strAge: $a, $strWeightKg: ${w}kg, $strHeightCm: ${h}cm, ${if (isMale) strMale else strFemale}",
                                                date = System.currentTimeMillis(),
                                                isEdited = true
                                            )
                                        )
                                    }
                                    onNavigateUp()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(strSave, fontSize = 16.sp)
                        }
                    }

                    //Edit Protein
                    "Protein" -> {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text(strInputName) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = proteinWeight,
                            onValueChange = { proteinWeight = it; proteinWeightError = "" },
                            label = { Text(strProteinWeight) },
                            isError = proteinWeightError.isNotEmpty(),
                            supportingText = {
                                if (proteinWeightError.isNotEmpty())
                                    Text(proteinWeightError, color = MaterialTheme.colorScheme.error)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Text(strGoal, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        goalLabels.forEachIndexed { index, label ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = selectedGoal == index,
                                    onClick = { selectedGoal = index }
                                )
                                Text(label, fontSize = 14.sp)
                            }
                        }

                        Button(
                            onClick = {
                                if (proteinWeight.isBlank()) {
                                    proteinWeightError = strErrorEmpty
                                } else if (proteinWeight.toDoubleOrNull() == null) {
                                    proteinWeightError = strErrorNumber
                                } else if (proteinWeight.toDouble() <= 0) {
                                    proteinWeightError = strErrorWeight
                                } else {
                                    val w = proteinWeight.toDouble()
                                    val multiplier = goalMultipliers[selectedGoal]
                                    val min = w * multiplier.first
                                    val max = w * multiplier.second
                                    item?.let {
                                        historyViewModel.update(
                                            it.copy(
                                                result = "${String.format(Locale.current.platformLocale, "%.0f", min)}-${String.format(Locale.current.platformLocale, "%.0f", max)}g/day",
                                                detail = "$strProteinWeight: ${w}kg, $strGoal: ${goalLabels[selectedGoal]}",
                                                date = System.currentTimeMillis(),
                                                isEdited = true
                                            )
                                        )
                                    }
                                    onNavigateUp()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(strSave, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}