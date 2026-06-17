package com.surya607062400013.asesmentmobpro1.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.surya607062400013.asesmentmobpro1.R
import com.surya607062400013.asesmentmobpro1.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val authState by authViewModel.authState.collectAsState()

    // Navigasi otomatis saat sukses login
    LaunchedEffect(authState) {
        if (authState is AuthViewModel.AuthState.Success) {
            onLoginSuccess()
        }
    }

    // Tampilkan error via Toast
    LaunchedEffect(authState) {
        if (authState is AuthViewModel.AuthState.Error) {
            val errorMsg = (authState as AuthViewModel.AuthState.Error).message
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            authViewModel.resetAuthState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo / Banner
            Image(
                painter = painterResource(id = R.drawable.banner2),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Nama Aplikasi
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = stringResource(R.string.home_tagline),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Tombol Sign In with Google
            OutlinedButton(
                onClick = {
                    scope.launch {
                        try {
                            val credentialManager = CredentialManager.create(context)
                            val request = authViewModel.getSignInRequest()
                            val result = credentialManager.getCredential(
                                context = context as android.app.Activity,
                                request = request
                            )
                            authViewModel.handleSignInResult(result)
                        } catch (e: GetCredentialCancellationException) {
                        } catch (e: NoCredentialException) {
                            authViewModel.handleSignInError(
                                "Tidak ada akun Google ditemukan. Pastikan Anda sudah login di perangkat."
                            )
                        } catch (e: GetCredentialException) {
                            authViewModel.handleSignInError(
                                "Gagal login: ${e.message}"
                            )
                        }
                    }
                },
                enabled = authState !is AuthViewModel.AuthState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.primary
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                if (authState is AuthViewModel.AuthState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.login_loading),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = stringResource(R.string.login_google),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Info text
            Text(
                text = stringResource(R.string.login_info),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }

        // Loading overlay
        AnimatedVisibility(
            visible = authState is AuthViewModel.AuthState.Loading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
