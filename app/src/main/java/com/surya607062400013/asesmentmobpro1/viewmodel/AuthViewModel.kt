package com.surya607062400013.asesmentmobpro1.viewmodel

import android.app.Application
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.surya607062400013.asesmentmobpro1.R
import com.surya607062400013.asesmentmobpro1.data.datastore.SettingsDataStore
import com.surya607062400013.asesmentmobpro1.utils.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStore = SettingsDataStore(application)

    val userName: Flow<String> = dataStore.userName
    val userEmail: Flow<String> = dataStore.userEmail
    val userPhotoUrl: Flow<String> = dataStore.userPhotoUrl
    val googleId: Flow<String> = dataStore.googleId
    val isLoggedIn: StateFlow<Boolean?> = dataStore.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val networkMonitor = NetworkMonitor(application)
    val isNetworkAvailable: StateFlow<Boolean> = networkMonitor.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        data class Error(val message: String) : AuthState()
    }

    fun getSignInRequest(): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId("400198194829-srerajkbq21o71t9ngnlboc05mfo2kkg.apps.googleusercontent.com")
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    fun handleSignInResult(result: GetCredentialResponse) {
        val credential = result.credential
        if (credential is androidx.credentials.CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val name = googleIdTokenCredential.displayName ?: ""
                val email = googleIdTokenCredential.id
                val photoUrl = googleIdTokenCredential.profilePictureUri?.toString() ?: ""
                val googleId = googleIdTokenCredential.id

                viewModelScope.launch {
                    _authState.value = AuthState.Loading
                    dataStore.saveUserData(name, email, photoUrl, googleId)
                    _authState.value = AuthState.Success
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(getApplication<Application>().getString(R.string.error_process_credential, e.message ?: ""))
            }
        } else {
            _authState.value = AuthState.Error(getApplication<Application>().getString(R.string.error_unknown_credential))
        }
    }

    fun handleSignInError(message: String) {
        _authState.value = AuthState.Error(message)
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    fun signOut() {
        resetAuthState()
        viewModelScope.launch {
            dataStore.clearUserData()
        }
    }
}
