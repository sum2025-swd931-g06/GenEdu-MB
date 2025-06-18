package com.example.mvvm.ui.screen.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.models.UserData
import com.example.mvvm.repositories.SharedPreferencesTokenProvider
import com.example.mvvm.repositories.apis.keycloak.KeycloakRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Success(val profile: UserData) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val keycloakRepository: KeycloakRepository,
    private val tokenProvider: SharedPreferencesTokenProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            try {
                Log.d("ProfileViewModel", "Fetching user profile")
                val token = tokenProvider.getToken()

                Log.d("ProfileViewModel", "Token available: ${token != null}")

                if (token == null) {
                    _uiState.value = ProfileUiState.Error("No token available")
                    return@launch
                }

                keycloakRepository.getUserInfo(token).fold(
                    onSuccess = { userData ->
                        Log.d("ProfileViewModel", "Profile fetch successful")
                        _uiState.value = ProfileUiState.Success(userData)
                    },
                    onFailure = { error ->
                        Log.e("ProfileViewModel", "Profile fetch failed: ${error.message}")
                        _uiState.value = ProfileUiState.Error(error.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching profile: ${e.message}")
                _uiState.value = ProfileUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun refresh() {
        fetchProfile()
    }

    fun logout(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val refreshToken = tokenProvider.getRefreshToken()

            if (refreshToken.isNullOrEmpty()) {
                // If we don't have a refresh token, just clear tokens and return success
                tokenProvider.clearTokens()
                onComplete(true)
                return@launch
            }

            keycloakRepository.logout(refreshToken).fold(
                onSuccess = {
                    onComplete(true)
                },
                onFailure = { error ->
                    // Even if the server-side logout fails, we still clear tokens locally
                    tokenProvider.clearTokens()
                    onComplete(true)
                }
            )
        }
    }
}