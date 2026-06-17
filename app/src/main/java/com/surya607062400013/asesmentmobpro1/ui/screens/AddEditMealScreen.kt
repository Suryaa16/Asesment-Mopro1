package com.surya607062400013.asesmentmobpro1.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.surya607062400013.asesmentmobpro1.R
import com.surya607062400013.asesmentmobpro1.data.local.entity.MealEntity
import com.surya607062400013.asesmentmobpro1.viewmodel.AuthViewModel
import com.surya607062400013.asesmentmobpro1.viewmodel.MealViewModel
import androidx.core.net.toUri
import android.content.Context
import androidx.core.content.FileProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import java.io.File
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMealScreen(
    mealId: Int,
    mealViewModel: MealViewModel,
    authViewModel: AuthViewModel,
    onNavigateUp: () -> Unit
) {
    val isEdit = mealId != -1
    val context = LocalContext.current
    // State untuk input form
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var existingImageUrl by remember { mutableStateOf<String?>(null) }
    var existingMeal by remember { mutableStateOf<MealEntity?>(null) }
    var googleId by remember { mutableStateOf("") }
    
    var nameError by remember { mutableStateOf<String?>(null) }
    var caloriesError by remember { mutableStateOf<String?>(null) }

    // Load data jika Edit Mode
    LaunchedEffect(mealId) {
        googleId = authViewModel.googleId.first()
        
        if (isEdit) {
            val meals = mealViewModel.meals.first()
            val mealToEdit = meals.find { it.id == mealId }
            if (mealToEdit != null) {
                existingMeal = mealToEdit
                name = mealToEdit.name
                description = mealToEdit.description
                calories = mealToEdit.calories.toString()
                existingImageUrl = mealToEdit.imageUrl
                if (mealToEdit.localImagePath != null) {
                    imageUri = mealToEdit.localImagePath.toUri()
                }
            }
        }
    }

    // Crop Image Launcher
    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            imageUri = result.uriContent
        }
    }

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            cropImageLauncher.launch(
                CropImageContractOptions(
                    uri = it,
                    cropImageOptions = CropImageOptions(
                        guidelines = CropImageView.Guidelines.ON,
                        allowRotation = true,
                        allowFlipping = true,
                        imageSourceIncludeGallery = false,
                        imageSourceIncludeCamera = false
                    )
                )
            )
        }
    }

    // Temporary URI untuk Kamera
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            tempCameraUri?.let {
                cropImageLauncher.launch(
                    CropImageContractOptions(
                        uri = it,
                        cropImageOptions = CropImageOptions(
                            guidelines = CropImageView.Guidelines.ON,
                            allowRotation = true,
                            allowFlipping = true,
                            imageSourceIncludeGallery = false,
                            imageSourceIncludeCamera = false
                        )
                    )
                )
            }
        }
    }

    var showImageSourceDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) stringResource(R.string.edit_meal) else stringResource(R.string.add_meal)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Kotak Pemilih Gambar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { showImageSourceDialog = true },
                contentAlignment = Alignment.Center
            ) {
                val imageToDisplay = imageUri?.toString() ?: existingImageUrl
                if (!imageToDisplay.isNullOrEmpty()) {
                    AsyncImage(
                        model = imageToDisplay,
                        contentDescription = "Meal Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentScale = ContentScale.FillWidth
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.take_choose_photo),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { 
                    name = it
                    if (nameError != null && it.isNotBlank()) nameError = null 
                },
                label = { Text(stringResource(R.string.meal_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = nameError != null,
                supportingText = { if (nameError != null) Text(nameError!!) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.meal_description)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = calories,
                onValueChange = { 
                    calories = it
                    if (caloriesError != null && it.toIntOrNull() != null && it.toInt() > 0) caloriesError = null
                },
                label = { Text(stringResource(R.string.meal_calories)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = caloriesError != null,
                supportingText = { if (caloriesError != null) Text(caloriesError!!) }
            )

            Spacer(modifier = Modifier.height(32.dp))

                val msgError = stringResource(R.string.error_fill_name_calories)
                val errorFieldRequired = stringResource(R.string.error_field_required)
                val errorInvalidCalories = stringResource(R.string.error_invalid_calories)
            Button(
                onClick = {
                    val cal = calories.toIntOrNull()
                    var isValid = true

                    if (name.isBlank()) {
                        nameError = errorFieldRequired
                        isValid = false
                    } else {
                        nameError = null
                    }

                    if (cal == null || cal <= 0) {
                        caloriesError = errorInvalidCalories
                        isValid = false
                    } else {
                        caloriesError = null
                    }

                    if (!isValid) {
                        Toast.makeText(context, msgError, Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (isEdit && existingMeal != null) {
                        // Update
                        val updatedMeal = existingMeal!!.copy(
                            name = name,
                            description = description,
                            calories = cal!!,
                            localImagePath = imageUri?.toString() ?: existingMeal!!.localImagePath
                        )
                        mealViewModel.updateMeal(updatedMeal)
                    } else {
                        // Add
                        val newMeal = MealEntity(
                            googleId = googleId,
                            name = name,
                            description = description,
                            calories = cal!!,
                            localImagePath = imageUri?.toString()
                        )
                        mealViewModel.addMeal(newMeal)
                    }
                    onNavigateUp()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.save),
                    fontSize = 16.sp
                )
            }
        }
    }

    // Dialog Pilihan Kamera / Galeri
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text(stringResource(R.string.choose_image_source)) },
            text = {
                Column {
                    Text(
                        text = stringResource(R.string.camera),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showImageSourceDialog = false
                                val uri = createTempImageUri(context)
                                tempCameraUri = uri
                                takePictureLauncher.launch(uri)
                            }
                            .padding(16.dp),
                        fontSize = 16.sp
                    )
                    Text(
                        text = stringResource(R.string.gallery),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showImageSourceDialog = false
                                imagePickerLauncher.launch("image/*")
                            }
                            .padding(16.dp),
                        fontSize = 16.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showImageSourceDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

fun createTempImageUri(context: Context): Uri {
    val tempFile = File.createTempFile("meal_image_", ".jpg", context.cacheDir)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        tempFile
    )
}
