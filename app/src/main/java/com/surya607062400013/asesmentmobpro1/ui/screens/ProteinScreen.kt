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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.surya607062400013.asesmentmobpro1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProteinScreen(onNavigateUp: () -> Unit) {

    // State input
    var weight by remember { mutableStateOf("") }
    var selectedGoal by remember { mutableIntStateOf(0) }

    // State error
    var weightError by remember { mutableStateOf("") }

    // State hasil
    var proteinMin by remember { mutableStateOf<Double?>(null) }
    var proteinMax by remember { mutableStateOf<Double?>(null) }

    val scrollState = rememberScrollState()

    // Goal labels & multiplier (min, max) per kg berat badan
    val goalLabels = listOf(
        stringResource(R.string.protein_sedentary),
        stringResource(R.string.protein_fitness),
        stringResource(R.string.protein_muscle),
        stringResource(R.string.protein_athlete)
    )
    // Pair(min per kg, max per kg)
    val goalMultipliers = listOf(
        Pair(0.8, 1.0),   // Sedentary
        Pair(1.2, 1.4),   // General Fitness
        Pair(1.6, 2.0),   // Muscle Building
        Pair(2.0, 2.4)    // Athlete
    )

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

            // ── Input Berat ──
            OutlinedTextField(
                value = weight,
                onValueChange = {
                    weight = it
                    weightError = ""
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

            // ── Pilih Tujuan (RadioButton) ──
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

            // ── Tombol Hitung ──
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
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(stringResource(R.string.protein_calculate), fontSize = 16.sp)
            }

            // ── Tampilkan Hasil ──
            if (proteinMin != null && proteinMax != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFCE93D8) // ungu - protein
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
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = String.format(java.util.Locale.getDefault(),"%.0f - %.0f", proteinMin, proteinMax),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.protein_gram),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        HorizontalDivider(color = Color.White.copy(alpha = 0.5f))

                        // Detail min max
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = stringResource(R.string.protein_min),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = String.format(java.util.Locale.getDefault(),"%.0f g", proteinMin),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = stringResource(R.string.protein_max),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = String.format(java.util.Locale.getDefault(),"%.0f g", proteinMax),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}