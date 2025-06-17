package com.example.mvvm

import androidx.lifecycle.ViewModel
import com.example.mvvm.models.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class MainState(
    val error: String = "",
    val isAuthenticated: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(MainState())
    private val _userData = MutableStateFlow<UserData?>(null)
    val uiState = _uiState.asStateFlow()
    val userData = _userData.asStateFlow()

    fun setError(message: String) {
        _uiState.value = _uiState.value.copy(error = message)
    }

    fun setAuthenticated(isAuthenticated: Boolean) {
        _uiState.value = _uiState.value.copy(isAuthenticated = isAuthenticated)
    }

    fun setUserData(data: UserData) {
        _userData.value = data
    }
}