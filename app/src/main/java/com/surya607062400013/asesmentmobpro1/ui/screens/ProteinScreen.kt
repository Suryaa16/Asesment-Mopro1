package com.surya607062400013.asesmentmobpro1.ui.screens

import android.content.Intent
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
import com.surya607062400013.asesmentmobpro1.R
import com.surya607062400013.asesmentmobpro1.viewmodel.HistoryViewModel
import androidx.compose.ui.text.intl.Locale
import com.surya607062400013.asesmentmobpro1.data.local.entity.HistoryEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProteinScreen(onNavigateUp: () -> Unit, historyViewModel: HistoryViewModel) {

    //State input
    var weight by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableIntStateOf(0) }
    var isSaved by remember { mutableStateOf(false) }
    //State error
    var weightError by remember { mutableStateOf("") }

    //State hasil
    var proteinMin by remember { mutableStateOf<Double?>(null) }
    var proteinMax by remember { mutableStateOf<Double?>(null) }
    var name by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val strInputName = stringResource(R.string.input_name)
    val strAnonymous = stringResource(R.string.anonymous)

    //Tujuan asupan protein (min, max) per kg berat badan
    val goalLabels = listOf(
        stringResource(R.string.protein_sedentary),
        stringResource(R.string.protein_fitness),
        stringResource(R.string.protein_muscle),
        stringResource(R.string.protein_athlete)
    )
    //min per kg, max per kg
    val goalMultipliers = listOf(
        Pair(0.8, 1.0),   // tidak olahraga
        Pair(1.2, 1.4),   // kebugaran umum
        Pair(1.6, 2.0),   // Pembentukan otot
        Pair(2.0, 2.4)    // Atlit
    )
    val context = LocalContext.current
    val shareProteinTemplate = stringResource(R.string.share_protein_result)
    val strWeightLabel = stringResource(R.string.protein_weight)
    val strGoalLabel = stringResource(R.string.protein_goal)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.protein_title)) },
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

            // Inputan
            OutlinedTextField(
                value = weight,
                onValueChange = {
                    weight = it
                    weightError = ""
                    isSaved = false
                },
                label = { Text(stringResource(R.string.protein_weight)) },
                isError = weightError.isNotEmpty(),
                supportingText = {
                    if (weightError.isNotEmpty()) Text(weightError, color = MaterialTheme.colorScheme.error)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            //Pilihan tujuan
            Text(
                text = stringResource(R.string.protein_goal),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            goalLabels.forEachIndexed { index, label ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = selectedGoal == index,
                        onClick = { selectedGoal = index }
                    )
                    Text(text = label, fontSize = 14.sp)
                }
            }

            //Hitung
            Button(
                onClick = {
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

                    if (valid) {
                        val w = weight.toDouble()
                        val multiplier = goalMultipliers[selectedGoal]
                        proteinMin = w * multiplier.first
                        proteinMax = w * multiplier.second

                        //Simpan ke database
                        if (!isSaved) {
                            historyViewModel.insert(
                                HistoryEntity(
                                    name = name.ifBlank { strAnonymous },
                                    type = "Protein",
                                    result = "${
                                        String.format(
                                            Locale.current.platformLocale,
                                            "%.0f",
                                            proteinMin
                                        )
                                    }-${
                                        String.format(
                                            Locale.current.platformLocale,
                                            "%.0f",
                                            proteinMax
                                        )
                                    }g/day",
                                    detail = "$strWeightLabel: ${w}kg, $strGoalLabel: ${goalLabels[selectedGoal]}",
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
                Text(stringResource(R.string.protein_calculate), fontSize = 16.sp)
            }

            //Tampilkan hasil
            if (proteinMin != null && proteinMax != null) {
                val min = proteinMin!!
                val max = proteinMax!!
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0D0D1A)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xFFBF00FF)
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
                            text = stringResource(R.string.protein_result),
                            fontSize = 14.sp,
                            color = Color(0xFF8888AA)
                        )
                        Text(
                            text = String.format(Locale.current.platformLocale, "%.0f - %.0f", min, max),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFBF00FF)
                        )
                        Text(
                            text = stringResource(R.string.protein_gram),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )

                        HorizontalDivider(color = Color(0xFFBF00FF).copy(alpha = 0.3f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = stringResource(R.string.protein_min),
                                    fontSize = 12.sp,
                                    color = Color(0xFF8888AA)
                                )
                                Text(
                                    text = String.format(Locale.current.platformLocale, "%.0f g", min),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFBF00FF)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = stringResource(R.string.protein_max),
                                    fontSize = 12.sp,
                                    color = Color(0xFF8888AA)
                                )
                                Text(
                                    text = String.format(Locale.current.platformLocale, "%.0f g", max),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFBF00FF)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        //Tombol share hasil
                        OutlinedButton(
                            onClick = {
                                val min = proteinMin ?: 0.0
                                val max = proteinMax ?: 0.0
                                val shareText = String.format(
                                    Locale.current.platformLocale,
                                    shareProteinTemplate,
                                    min,
                                    max
                                )
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                }
                                context.startActivity(Intent.createChooser(intent, "Share via"))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(
                                1.dp, Color(0xFFBF00FF)
                            )
                        ) {
                            Text(stringResource(R.string.menu_share_result),color = Color(0xFFBF00FF),
                                fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}