package com.surya607062400013.asesmentmobpro1.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import java.sql.Date
import java.text.SimpleDateFormat
import androidx.compose.ui.platform.LocalLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onNavigateUp: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToRecycleBin: () -> Unit,
    historyViewModel: HistoryViewModel
) {
    val allHistory by historyViewModel.allHistory.collectAsStateWithLifecycle(emptyList())
    val selectedFilter by historyViewModel.selectedFilter.collectAsStateWithLifecycle()

    //Dialog konfirmasi hapus
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<HistoryEntity?>(null) }

    //Filter history
    val filteredHistory = when (selectedFilter) {
        "BMI" -> allHistory.filter { it.type == "BMI" }
        "Calorie" -> allHistory.filter { it.type == "Calorie" }
        "Protein" -> allHistory.filter { it.type == "Protein" }
        else -> allHistory
    }

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
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onNavigateToRecycleBin) {
                        Text(stringResource(R.string.recycle_bin))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
                                when (filter) {
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

            //List atau empty state
            if (filteredHistory.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = stringResource(R.string.history_empty),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
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

    //Warna berdasarkan type
    val typeColor = when (item.type) {
        "BMI" -> Color(0xFF90CAF9)
        "Calorie" -> Color(0xFFA5D6A7)
        "Protein" -> Color(0xFFCE93D8)
        else -> Color(0xFFE0E0E0)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
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
                    color = typeColor
                ) {
                    Text(
                        text = item.type,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(
                        text = item.result,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = dateStr,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            //Tombol hapus
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}