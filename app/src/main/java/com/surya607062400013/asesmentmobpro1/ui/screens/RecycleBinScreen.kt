package com.surya607062400013.asesmentmobpro1.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextOverflow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(
    onNavigateUp: () -> Unit,
    historyViewModel: HistoryViewModel
) {
    val recycleBin by historyViewModel.recycleBin.collectAsStateWithLifecycle(emptyList())

    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<HistoryEntity?>(null) }

    //Dialog hapus permanen
    if (showDeleteDialog && itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_permanent)) },
            text = { Text(stringResource(R.string.delete_permanent_desc)) },
            confirmButton = {
                TextButton(onClick = {
                    itemToDelete?.let { historyViewModel.deletePermanent(it.id) }
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
                title = { Text(stringResource(R.string.recycle_bin)) },
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
        if (recycleBin.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.recycle_bin_empty),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recycleBin, key = { it.id }) { item ->
                    RecycleBinItem(
                        item = item,
                        onRestore = { historyViewModel.restore(item.id) },
                        onDeletePermanent = {
                            itemToDelete = item
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RecycleBinItem(
    item: HistoryEntity,
    onRestore: () -> Unit,
    onDeletePermanent: () -> Unit
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = typeBorderColor.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Badge type
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = typeColor,
                    border = BorderStroke(1.dp, typeBorderColor
                )
                ) {
                    Text(
                        text = item.type,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = typeBorderColor
                    )
                }
                Text(
                    text = dateStr,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = item.result,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = typeBorderColor
            )

            Text(
                text = item.detail,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Tombol Restore
                OutlinedButton(
                    onClick = onRestore,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    border = BorderStroke(
                        1.dp, Color(0xFF39FF14)
                    )
                ) {
                    Text(
                        stringResource(R.string.restore),
                        fontSize = 13.sp
                    )
                }

                //Tombol Hapus Permanen
                Button(
                    onClick = onDeletePermanent,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.delete_permanent),
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}