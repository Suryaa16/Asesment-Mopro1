package com.surya607062400013.asesmentmobpro1.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.surya607062400013.asesmentmobpro1.viewmodel.HistoryViewModel

@Composable
fun HistoryDetailScreen(
    id: Int,
    onNavigateUp: () -> Unit,
    onNavigateToEdit: () -> Unit,
    historyViewModel: HistoryViewModel
) {
    Text("History Detail Screen")
}