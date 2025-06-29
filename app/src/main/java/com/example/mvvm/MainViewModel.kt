package com.example.mvvm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.models.UserData
import com.example.mvvm.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainState(
    val error: String = "",
    val isAuthenticated: Boolean = false
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainState())
    private val _userData = MutableStateFlow<UserData?>(null)
    val uiState = _uiState.asStateFlow()
    val userData = _userData.asStateFlow()

    init {
        checkIfUserIsLoggedIn()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkIfUserIsLoggedIn() {
        viewModelScope.launch {
            val token = authRepository.getStoredToken()
            val isValid = token != null && authRepository.isTokenValid(token)
            _uiState.value = _uiState.value.copy(isAuthenticated = isValid)
        }
    }

    fun setError(message: String) {
        _uiState.value = _uiState.value.copy(error = message)
    }

    fun setAuthenticated(isAuthenticated: Boolean) {
        _uiState.value = _uiState.value.copy(isAuthenticated = isAuthenticated)
    }

    fun setUserData(data: UserData?) {
        _userData.value = data
    }
}