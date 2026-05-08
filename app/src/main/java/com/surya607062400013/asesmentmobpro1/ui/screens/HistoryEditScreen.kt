package com.surya607062400013.asesmentmobpro1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
    var editedResult by remember {mutableStateOf("") }
    var editedDetail by remember {mutableStateOf("") }
    var resultError by remember {mutableStateOf("") }
    var isLoaded by remember {mutableStateOf(false) }

    //Load data existing
    LaunchedEffect(id) {
        historyViewModel.getById(id) { result ->
            item = result
            //Tampilan data existing di form
            editedResult = result?.result ?: ""
            editedDetail = result?.detail ?: ""
            isLoaded = true
        }
    }

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

                // Edit Result
                OutlinedTextField(
                    value = editedResult,
                    onValueChange = {
                        editedResult = it
                        resultError = ""
                    },
                    label = { Text("Result") },
                    isError = resultError.isNotEmpty(),
                    supportingText = {
                        if (resultError.isNotEmpty()) Text(
                            resultError,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Edit Detail
                OutlinedTextField(
                    value = editedDetail,
                    onValueChange = { editedDetail = it },
                    label = { Text("Detail") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Tombol Simpan
                Button(
                    onClick = {
                        if (editedResult.isBlank()) {
                            resultError = "This field cannot be empty"
                        } else {
                            item?.let {
                                historyViewModel.update(
                                    it.copy(
                                        result = editedResult,
                                        detail = editedDetail
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
                    Text("Save", fontSize = 16.sp)
                }
            }
        }
    }
}