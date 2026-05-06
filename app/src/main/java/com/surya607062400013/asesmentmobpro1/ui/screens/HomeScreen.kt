package com.surya607062400013.asesmentmobpro1.ui.screens

import android.R.id.shareText
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.surya607062400013.asesmentmobpro1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToBmi: () -> Unit,
    onNavigateToCalorie: () -> Unit,
    onNavigateToProtein: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    //Menu icon
                    IconButton(onClick = { showMenu = true}) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                    //Dropdown menu
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.menu_about)) },
                            onClick = {
                                showMenu = false
                                onNavigateToAbout()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.menu_share)) },
                            onClick = {
                                showMenu = false
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        shareText
                                    )
                                }
                                context.startActivity(
                                    Intent.createChooser(shareIntent, "Share via")
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("History") },
                            onClick = {
                                showMenu = false
                                onNavigateToHistory()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Settings") },
                            onClick = {
                                showMenu = false
                                onNavigateToSettings()
                            }
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //gambar Banner
            Image(
                painter = painterResource(id = R.drawable.banner1),
                contentDescription = "Banner",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(24.dp))

            //Tagline
            Text(
                text = stringResource(R.string.home_tagline),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            //Subtitle
            Text(
                text = stringResource(R.string.home_subtitle),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))

            //Tombol BMI
            Button(
                onClick = onNavigateToBmi,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_bmi),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            //Tombol Kalori
            Button(
                onClick = onNavigateToCalorie,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_calorie),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            //Tombol protein
            Button(
                onClick = onNavigateToProtein,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.btn_protein),
                    fontSize = 16.sp
                )
            }
        }
    }
}