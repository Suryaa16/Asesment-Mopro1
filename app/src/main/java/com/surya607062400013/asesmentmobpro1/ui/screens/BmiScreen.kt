package com.surya607062400013.asesmentmobpro1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.text.intl.Locale
import com.surya607062400013.asesmentmobpro1.R
import com.surya607062400013.asesmentmobpro1.viewmodel.HistoryViewModel
import com.surya607062400013.asesmentmobpro1.data.local.entity.HistoryEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BmiScreen(onNavigateUp: () -> Unit, historyViewModel: HistoryViewModel) {
    //State input
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var isMetric by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }

    //State error
    var weightError by remember { mutableStateOf("") }
    var heigthError by remember { mutableStateOf("") }

    //State hasil
    var bmiResult by remember { mutableStateOf<Double?>(null) }
    var bmiCategory by remember { mutableStateOf("") }
    var isSaved by remember { mutableStateOf(false) }

    //Mengambil string dari strings.xml agar bisa multibahasa
    val scrollState = rememberScrollState()
    val strUnderweight = stringResource(R.string.bmi_underweight)
    val strNormal = stringResource(R.string.bmi_normal)
    val strOverweight = stringResource(R.string.bmi_overweight)
    val strObese = stringResource(R.string.bmi_obese)
    val context = LocalContext.current
    val shareBmiTemplate = stringResource(R.string.share_bmi_result)
    val strWeightLabel = stringResource(R.string.bmi_weight)
    val strHeightLabel = stringResource(R.string.bmi_height)
    val strInputName = stringResource(R.string.input_name)
    val strAnonymous = stringResource(R.string.anonymous)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.bmi_title)) },
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
            Text(
                text = "Unit System",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = isMetric,
                    onClick = {
                        isMetric = true
                        weight = ""
                        height = ""
                        bmiResult = null
                        isSaved = false
                    }
                )
                Text(stringResource(R.string.bmi_metric))
                Spacer(modifier = Modifier.width(24.dp))

                RadioButton(
                    selected = !isMetric,
                    onClick = {
                        isMetric = false
                        weight = ""
                        height = ""
                        bmiResult = null
                    }
                )
                Text(stringResource(R.string.bmi_imperial))
            }
            //Input nama
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(strInputName) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            //Input berat
            OutlinedTextField(
                value = weight,
                onValueChange = {
                    weight = it
                    weightError = ""
                    isSaved = false
                },
                label = {
                    Text(
                        if (isMetric) stringResource(R.string.bmi_weight_hint_metric)
                        else stringResource(R.string.bmi_weight_hint_imperial)
                    )
                },
                isError = weightError.isNotEmpty(),
                supportingText = {
                    if (weightError.isNotEmpty()) Text(weightError, color = MaterialTheme.colorScheme.error)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            //Input tinggi
            OutlinedTextField(
                value = height,
                onValueChange = {
                    height = it
                    heigthError = ""
                    isSaved = false
                },
                label = {
                    Text(
                        if (isMetric) stringResource(R.string.bmi_height_hint_metric)
                        else stringResource(R.string.bmi_height_hint_imperial)
                    )
                },
                isError = heigthError.isNotEmpty(),
                supportingText = {
                    if (heigthError.isNotEmpty()) Text(heigthError, color = MaterialTheme.colorScheme.error)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            //Tombol hitung
            Button(
                onClick = {
                    //validasi
                    var valid = true

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

                    if (height.isBlank()) {
                        heigthError = "This field cannot be empty"
                        valid = false
                    } else if (height.toDoubleOrNull() == null) {
                        heigthError = "Please enter a valid number"
                        valid = false
                    } else if (height.toDouble() <= 0) {
                        heigthError = "Height must be greater than 0"
                        valid = false
                    }

                    if (valid) {
                        val w = weight.toDouble()
                        val h = height.toDouble()

                        //Hitung BMI = berat badan : tinggi badan x tinggi badan
                        val bmi = if (isMetric) {
                            val hMeters = h / 100.0
                            w / (hMeters * hMeters)
                        } else {
                            703 * w / (h * h)
                        }

                        bmiResult = bmi
                        //Kategori
                        bmiCategory = when {
                            bmi < 18.5 -> strUnderweight
                            bmi < 25.0 -> strNormal
                            bmi < 30.0 -> strOverweight
                            else -> strObese
                        }

                        //Simpan ke database
                        if (!isSaved) {
                            historyViewModel.insert(
                                HistoryEntity(
                                    name = name.ifBlank { strAnonymous },
                                    type = "BMI",
                                    result = "BMI: ${
                                        String.format(
                                            Locale.current.platformLocale,
                                            "%.1f",
                                            bmi
                                        )
                                    } - $bmiCategory",
                                    detail = "$strWeightLabel: ${w}${if (isMetric) "kg" else "lbs"}, $strHeightLabel: ${h}${if (isMetric) "cm" else "in"}, Unit: ${if (isMetric) "Metric" else "Imperial"}",
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
                Text(stringResource(R.string.bmi_calculate), fontSize = 16.sp)
            }

            //Tampilan Hasil
            bmiResult?.let { bmi ->
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0D0D1A)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = when {
                            bmi < 18.5 -> Color(0xFF00E5FF)
                            bmi < 25.0 -> Color(0xFF39FF14)
                            bmi < 30.0 -> Color(0xFFFFD600)
                            else -> Color(0xFFFF1744)
                        }
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
                            text = stringResource(R.string.bmi_result),
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = String.format(Locale.current.platformLocale, "%.1f", bmi),
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                bmi < 18.5 -> Color(0xFF00E5FF)
                                bmi < 25.0 -> Color(0xFF39FF14)
                                bmi < 30.0 -> Color(0xFFFFD600)
                                else -> Color(0xFFFF1744)
                            }
                        )
                        Text(
                            text = bmiCategory,
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
                                    shareBmiTemplate,
                                    bmi,
                                    bmiCategory
                                )
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                }
                                context.startActivity(Intent.createChooser(intent, "Share via"))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(
                                1.dp, when {
                                    bmi < 18.5 -> Color(0xFF00E5FF)
                                    bmi < 25.0 -> Color(0xFF39FF14)
                                    bmi < 30.0 -> Color(0xFFFFD600)
                                    else -> Color(0xFFFF1744)
                                }
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.menu_share),
                                color = when {
                                    bmi < 18.5 -> Color(0xFF00E5FF)
                                    bmi < 25.0 -> Color(0xFF39FF14)
                                    bmi < 30.0 -> Color(0xFFFFD600)
                                    else -> Color(0xFFFF1744)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}