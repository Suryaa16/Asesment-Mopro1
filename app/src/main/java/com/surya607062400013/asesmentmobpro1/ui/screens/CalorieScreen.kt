package com.surya607062400013.asesmentmobpro1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.intl.Locale
import com.surya607062400013.asesmentmobpro1.R
import com.surya607062400013.asesmentmobpro1.viewmodel.HistoryViewModel
import com.surya607062400013.asesmentmobpro1.data.local.entity.HistoryEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalorieScreen(onNavigateUp: () -> Unit, historyViewModel: HistoryViewModel) {
    //stage input
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var isMale by remember { mutableStateOf(true) }
    var selectedActivity by remember { mutableIntStateOf(0) }
    var name by remember { mutableStateOf("") }

    //state error
    var ageError by remember { mutableStateOf("") }
    var weightError by remember { mutableStateOf("") }
    var heightError by remember { mutableStateOf("") }
    var isSaved by remember { mutableStateOf(false) }

    //state hasil
    var calorieResult by remember {mutableStateOf<Double?>(null)}
    val scrollState = rememberScrollState()

    //List aktivitas
    val activityLabels = listOf(
        stringResource(R.string.calorie_sedentary),
        stringResource(R.string.calorie_light),
        stringResource(R.string.calorie_moderate),
        stringResource(R.string.calorie_active),
        stringResource(R.string.calorie_very_active),
    )
    val activityMultipliers = listOf(1.2, 1.375, 1.55, 1.55, 1.9)
    val context = LocalContext.current
    val shareCalorieTemplate = stringResource(R.string.share_calorie_result)
    val strAgeLabel = stringResource(R.string.calorie_age)
    val strWeightLabel = stringResource(R.string.calorie_weight_kg)
    val strHeightLabel = stringResource(R.string.calorie_height_cm)
    val strMaleLabel = stringResource(R.string.calorie_male)
    val strFemaleLabel = stringResource(R.string.calorie_female)
    val strInputName = stringResource(R.string.input_name)
    val strAnonymous = stringResource(R.string.anonymous)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.calorie_title)) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //Input nama
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(strInputName) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            //input Usia
            OutlinedTextField(
                value = age,
                onValueChange = {
                    age = it
                    ageError = ""
                    isSaved = false
                },
                label = { Text(stringResource(R.string.calorie_age)) },
                isError = ageError.isNotEmpty(),
                supportingText = {
                    if (ageError.isNotEmpty()) Text(ageError, color = MaterialTheme.colorScheme.error)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            //input berat
            OutlinedTextField(
                value = weight,
                onValueChange = {
                    weight = it
                    weightError = ""
                    isSaved = false
                },
                label = { Text(stringResource(R.string.calorie_weight_kg)) },
                isError = weightError.isNotEmpty(),
                supportingText = {
                    if (weightError.isNotEmpty()) Text(weightError, color = MaterialTheme.colorScheme.error)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            //input tinggi
            OutlinedTextField(
                value = height,
                onValueChange = {
                    height = it
                    heightError = ""
                    isSaved = false
                },
                label = { Text(stringResource(R.string.calorie_height_cm)) },
                isError = heightError.isNotEmpty(),
                supportingText = {
                    if (heightError.isNotEmpty()) Text(heightError, color = MaterialTheme.colorScheme.error)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            //Gender
            Text(
                text = stringResource(R.string.calorie_gender),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = isMale,
                    onClick = { isMale = true}
                )
                Text(stringResource(R.string.calorie_male))
                Spacer(modifier = Modifier.width(24.dp))

                RadioButton(
                    selected = !isMale,
                    onClick = { isMale = false}
                )
                Text(stringResource(R.string.calorie_female))
            }

            //pilihan aktivitas
            Text(
                text = stringResource(R.string.calorie_activity),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            activityLabels.forEachIndexed { index, label ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = selectedActivity == index,
                        onClick = { selectedActivity = index }
                    )
                    Text(
                        text = label,
                        fontSize = 14.sp
                    )
                }
            }

            //Tombol hitung
            Button(
                onClick = {
                    var valid = true

                    //Validasi usia
                    if (age.isBlank()) {
                        ageError = "This field cannot be empty"
                        valid = false
                    } else if (age.toIntOrNull() == null) {
                        ageError = "Please enter a valid number"
                        valid = false
                    } else if (age.toInt() !in 1..120) {
                        ageError = "Age must be between 1 and 120"
                        valid = false
                    }

                    //Validasi berat
                    if (weight.isBlank()) {
                        weightError = "This field cannot be empty"
                        valid = false
                    } else if (weight.toDoubleOrNull() == null) {
                        weightError = "Please enter a valid number"
                        valid = false
                    } else if (weight.toDouble() <= 0) {
                        weightError = "Weight must be greater than 0"
                        valid = false
                    }

                    //Validasi tinggi
                    if (height.isBlank()) {
                        heightError = "This field cannot be empty"
                        valid = false
                    } else if (height.toDoubleOrNull() == null) {
                        heightError = "Please enter a valid number"
                        valid = false
                    } else if (height.toDouble() <= 0) {
                        heightError = "Height must be greater than 0"
                        valid = false
                    }

                    if (valid) {
                        val a = age.toInt()
                        val w = weight.toDouble()
                        val h = height.toDouble()

                        //Memakai rumus Revised Harris-Benedict Equation
                        val bmr = if (isMale) {
                            88.362 + (13.397 * w) + (4.799 * h) - (5.677 * a)
                        } else {
                            447.593 + (9.247 * w) + (3.098 * h) - (4.330 * a)
                        }
                        calorieResult = bmr * activityMultipliers[selectedActivity]

                        //Simpan ke database
                        if (!isSaved) {
                            historyViewModel.insert(
                                HistoryEntity(
                                    name = name.ifBlank { strAnonymous },
                                    type = "Calorie",
                                    result = "${
                                        String.format(
                                            Locale.current.platformLocale,
                                            "%.0f",
                                            calorieResult
                                        )
                                    } kcal/day",
                                    detail = "$strAgeLabel: $a, $strWeightLabel: ${w}kg, $strHeightLabel: ${h}cm, ${if (isMale) strMaleLabel else strFemaleLabel}",
                                    date = System.currentTimeMillis()
                                )
                            )
                        }
                        isSaved = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(R.string.calorie_calculate), fontSize = 16.sp)
            }

            //Hasilnya
            calorieResult?.let { calories ->
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0D0D1A)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xFF00E5FF)  // cyan neon
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.calorie_result),
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = String.format(Locale.current.platformLocale, "%.0f", calories),
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00E5FF)
                        )
                        Text(
                            text = stringResource(R.string.calorie_kcal),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        //Tombol share hasil
                        OutlinedButton(
                            onClick = {
                                val shareText = String.format(
                                    Locale.current.platformLocale,
                                    shareCalorieTemplate,
                                    calories
                                )
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                }
                                context.startActivity(Intent.createChooser(intent, "Share via"))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(
                                1.dp, Color(0xFF00E5FF)
                            )
                        ) {
                            Text(stringResource(R.string.menu_share_result),color = Color(0xFF00E5FF),
                                fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}