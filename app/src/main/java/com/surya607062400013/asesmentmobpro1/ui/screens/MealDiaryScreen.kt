package com.surya607062400013.asesmentmobpro1.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.imageLoader
import com.surya607062400013.asesmentmobpro1.R
import com.surya607062400013.asesmentmobpro1.data.local.entity.MealEntity
import com.surya607062400013.asesmentmobpro1.viewmodel.MealViewModel
import kotlinx.coroutines.launch
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDiaryScreen(
    mealViewModel: MealViewModel,
    rootPadding: PaddingValues,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val meals by mealViewModel.meals.collectAsState(initial = emptyList())
    val isLoading by mealViewModel.isLoading.collectAsState()
    val errorMessage by mealViewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            mealViewModel.clearError()
        }
    }

    val shouldShowImages by mealViewModel.hasBeenOnline.collectAsState()

    var showDeleteConfirmDialog by remember { mutableStateOf<MealEntity?>(null) }

    Scaffold(
        modifier = Modifier.padding(bottom = rootPadding.calculateBottomPadding()),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.bottom_nav_meal_diary)) },
                actions = {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(24.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add Meal")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding()
                )
        ) {
            if (meals.isEmpty() && !isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.meal_no_records),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(meals) { meal ->
                        MealItemCard(
                            meal = meal,
                            shouldShowImages = shouldShowImages,
                            onEditClick = { onNavigateToEdit(meal.id) },
                            onDeleteClick = { showDeleteConfirmDialog = meal }
                        )
                    }
                }
            }
        }
    }

    // Dialog Konfirmasi Hapus
    showDeleteConfirmDialog?.let { meal ->
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = null },
            title = { Text(stringResource(R.string.delete_confirm)) },
            text = { Text(stringResource(R.string.delete_confirm_meal_desc)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        mealViewModel.deleteMeal(meal)
                        showDeleteConfirmDialog = null
                    }
                ) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun MealItemCard(
    meal: MealEntity,
    shouldShowImages: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val shareTitle = stringResource(R.string.meal_share_title)
    val preparingImage = stringResource(R.string.meal_preparing_image)
    val shareText = stringResource(R.string.meal_share_text, meal.name, meal.calories, meal.description)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageSource = meal.localImagePath ?: meal.imageUrl
            if (!imageSource.isNullOrEmpty()) {
                if (!shouldShowImages) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = androidx.compose.ui.res.painterResource(id = R.drawable.broken_image),
                            contentDescription = stringResource(R.string.meal_broken_image),
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                } else {
                    val ctx = LocalContext.current
                    val memoryOnlyImageLoader = remember {
                        coil.ImageLoader.Builder(ctx)
                            .memoryCachePolicy(coil.request.CachePolicy.ENABLED)
                            .diskCachePolicy(coil.request.CachePolicy.DISABLED)
                            .okHttpClient {
                                okhttp3.OkHttpClient.Builder()
                                    .cache(null)
                                    .build()
                            }
                            .build()
                    }

                    coil.compose.SubcomposeAsyncImage(
                        model = imageSource,
                        imageLoader = memoryOnlyImageLoader,
                        contentDescription = meal.name,
                        loading = {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.primary,
                                    strokeWidth = 2.dp
                                )
                            }
                        },
                        error = {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(
                                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.broken_image),
                                    contentDescription = stringResource(R.string.meal_broken_image),
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            }
                        },
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = meal.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${meal.calories} kkal",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (meal.description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = meal.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!meal.isSynced) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.meal_not_synced),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    val imageSource = meal.localImagePath ?: meal.imageUrl
                    if (imageSource.isNullOrEmpty()) {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT, 
                                shareText
                            )
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, shareTitle)
                        context.startActivity(shareIntent)
                    } else {
                        coroutineScope.launch {
                            try {
                                Toast.makeText(context, preparingImage, Toast.LENGTH_SHORT).show()
                                val imageLoader = context.imageLoader
                                val request = coil.request.ImageRequest.Builder(context)
                                    .data(imageSource)
                                    .allowHardware(false)
                                    .build()
                                val result = imageLoader.execute(request)
                                if (result is coil.request.SuccessResult) {
                                    val bitmap = result.drawable.toBitmap()
                                    val file = File(context.cacheDir, "shared_meal.png")
                                    FileOutputStream(file).use { out ->
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                                    }
                                    val uri = FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.fileprovider",
                                        file
                                    )
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_STREAM, uri)
                                        putExtra(
                                            Intent.EXTRA_TEXT, 
                                            shareText
                                        )
                                        type = "image/png"
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, shareTitle)
                                    context.startActivity(shareIntent)
                                } else {
                                    Toast.makeText(context, "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                Toast.makeText(context, "Terjadi kesalahan saat membagikan", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
