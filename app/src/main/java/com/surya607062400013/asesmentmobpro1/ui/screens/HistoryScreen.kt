package com.surya607062400013.asesmentmobpro1.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.surya607062400013.asesmentmobpro1.R
import com.surya607062400013.asesmentmobpro1.data.local.entity.HistoryEntity
import com.surya607062400013.asesmentmobpro1.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import androidx.compose.ui.platform.LocalLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToRecycleBin: () -> Unit,
    rootPadding: PaddingValues,
    historyViewModel: HistoryViewModel
) {
    val allHistory by historyViewModel.allHistory.collectAsStateWithLifecycle(emptyList())
    val selectedFilter by historyViewModel.selectedFilter.collectAsStateWithLifecycle()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<HistoryEntity?>(null) }

    //Filter logic
    val filteredHistory = when (selectedFilter) {
        "BMI" -> allHistory.filter { it.type == "BMI" }
        "Calorie" -> allHistory.filter { it.type == "Calorie" }
        "Protein" -> allHistory.filter { it.type == "Protein" }
        else -> allHistory
    }

    //Dialog Konfirmasi Hapus
    if (showDeleteDialog && itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_confirm)) },
            text = { Text(stringResource(R.string.delete_confirm_desc)) },
            confirmButton = {
                TextButton(onClick = {
                    itemToDelete?.let { historyViewModel.softDelete(it.id) }
                    showDeleteDialog = false
                    itemToDelete = null
                }) {
                    Text(stringResource(R.string.confirm), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.history_title)) },
                actions = {
                    TextButton(onClick = onNavigateToRecycleBin) {
                        Text(stringResource(R.string.recycle_bin))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            //Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("All", "BMI", "Calorie", "Protein").forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { historyViewModel.setFilter(filter) },
                        label = {
                            Text(
                                text = when (filter) {
                                    "All" -> stringResource(R.string.filter_all)
                                    "BMI" -> stringResource(R.string.filter_bmi)
                                    "Calorie" -> stringResource(R.string.filter_calorie)
                                    else -> stringResource(R.string.filter_protein)
                                }
                            )
                        }
                    )
                }
            }

            if (filteredHistory.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.history_empty),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = rootPadding.calculateBottomPadding() + 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredHistory, key = { it.id }) { item ->
                        HistoryItem(
                            item = item,
                            onClick = { onNavigateToDetail(item.id) },
                            onDelete = {
                                itemToDelete = item
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(
    item: HistoryEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", LocalLocale.current.platformLocale)
    val dateStr = dateFormat.format(Date(item.date))

    val typeColor = when (item.type) {
        "BMI" -> Color(0xFF00E5FF).copy(alpha = 0.15f)
        "Calorie" -> Color(0xFF39FF14).copy(alpha = 0.15f)
        "Protein" -> Color(0xFFBF00FF).copy(alpha = 0.15f)
        else -> Color(0xFF8888AA).copy(alpha = 0.15f)
    }
    val typeBorderColor = when (item.type) {
        "BMI" -> Color(0xFF00E5FF)
        "Calorie" -> Color(0xFF39FF14)
        "Protein" -> Color(0xFFBF00FF)
        else -> Color(0xFF8888AA)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = typeBorderColor.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = typeColor,
                    border = BorderStroke(
                        1.dp, typeBorderColor
                    )
                ) {
                    Text(
                        text = item.type,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = typeBorderColor
                    )
                }
                Column {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.result,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        //Badge edited
                        if (item.isEdited) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.tertiary
                            ) {
                                Text(
                                    text = stringResource(R.string.edited),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onTertiary
                                )
                            }
                        }
                    }
                    //Date
                    Text(
                        text = dateStr,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                )
            }
        }
    }
}