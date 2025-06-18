package com.example.mvvm.ui.screen.intro

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.MainViewModel
import com.example.mvvm.configs.KeycloakAuthConfig
import com.example.mvvm.enum.LoadStatus
import com.example.mvvm.models.UserData
import com.example.mvvm.repositories.AuthRepository
import com.example.mvvm.repositories.SharedPreferencesTokenProvider
import com.example.mvvm.repositories.apis.keycloak.KeycloakRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import javax.inject.Inject

data class LoginUiState(
    val isAuthenticated: Boolean = false,
    val status: LoadStatus = LoadStatus.Init(),
    val accessToken: String? = null,
    val userData: UserData? = null
)

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class IntroViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val keycloakRepository: KeycloakRepository,
    private val tokenProvider: SharedPreferencesTokenProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private var authService: AuthorizationService? = null
    private var serviceConfig: AuthorizationServiceConfiguration? = null

    init {
        initializeAuthService()
        checkExistingAuth()
    }

    fun updateMainViewModel(
        mainViewModel: MainViewModel,
        userData: UserData,
        isAuthenticated: Boolean
    ) {
        mainViewModel.setUserData(userData)
        mainViewModel.setAuthenticated(isAuthenticated)
    }

    private fun initializeAuthService() {
        viewModelScope.launch {
            try {
                serviceConfig = AuthorizationServiceConfiguration(
                    Uri.parse(KeycloakAuthConfig.AUTH_ENDPOINT),
                    Uri.parse(KeycloakAuthConfig.TOKEN_ENDPOINT)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    status = LoadStatus.Error("Failed to initialize auth service: ${e.message}")
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkExistingAuth() {
        viewModelScope.launch {
            val existingToken = authRepository.getStoredToken()
            if (existingToken != null && authRepository.isTokenValid(existingToken)) {
                _uiState.value = _uiState.value.copy(
                    isAuthenticated = true,
                    accessToken = existingToken
                )
            }
        }
    }

    fun startLogin(context: Context, authResultLauncher: ActivityResultLauncher<Intent>?) {
        serviceConfig?.let { config ->
            _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())

            authService = AuthorizationService(context)

            val authRequest = AuthorizationRequest.Builder(
                config,
                KeycloakAuthConfig.CLIENT_ID,
                ResponseTypeValues.CODE,
                Uri.parse(KeycloakAuthConfig.REDIRECT_URI)
            )
                .setScope(KeycloakAuthConfig.SCOPE)
                .build()

            val authIntent = authService!!.getAuthorizationRequestIntent(authRequest)
            authResultLauncher?.launch(authIntent)
        } ?: run {
            _uiState.value = _uiState.value.copy(
                status = LoadStatus.Error("Auth service not initialized")
            )
        }
    }

    fun handleAuthResult(intent: Intent?) {
        if (intent == null) {
            _uiState.value = _uiState.value.copy(
                status = LoadStatus.Error("No auth result received")
            )
            return
        }

        val response = AuthorizationResponse.fromIntent(intent)
        val exception = AuthorizationException.fromIntent(intent)

        when {
            response != null -> {
                exchangeTokens(response)
            }
            exception != null -> {
                _uiState.value = _uiState.value.copy(
                    status = LoadStatus.Error("Auth failed: ${exception.message}")
                )
            }
            else -> {
                _uiState.value = _uiState.value.copy(
                    status = LoadStatus.Error("Unknown authentication error")
                )
            }
        }
    }

    private fun exchangeTokens(authResponse: AuthorizationResponse) {
        val tokenRequest = authResponse.createTokenExchangeRequest()

        _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())

        authService?.performTokenRequest(tokenRequest) { tokenResponse, exception ->
            viewModelScope.launch {
                try {
                    if (tokenResponse != null) {
                        val accessToken = tokenResponse.accessToken
                        val refreshToken = tokenResponse.refreshToken

                        if (accessToken != null) {
                            // Store both tokens
                            authRepository.storeToken(accessToken)

                            // Also store refresh token - add this method to AuthRepository
                            if (refreshToken != null) {
                                tokenProvider.saveRefreshToken(refreshToken)
                            }

                            // Token introspection with timeout
                            withTimeout(5000) {
                                keycloakRepository.introspectToken(accessToken).fold(
                                    onSuccess = { userData ->
                                        // Update both ViewModels
                                        _uiState.value = _uiState.value.copy(
                                            isAuthenticated = true,
                                            accessToken = accessToken,
                                            userData = userData,
                                            status = LoadStatus.Success()
                                        )
                                    },
                                    onFailure = { error ->
                                        _uiState.value = _uiState.value.copy(
                                            status = LoadStatus.Error("Token validation failed: ${error.message}")
                                        )
                                        tokenProvider.clearTokens()
                                    }
                                )
                            }
                        } else {
                            _uiState.value = _uiState.value.copy(
                                status = LoadStatus.Error("No access token received")
                            )
                        }
                    } else if (exception != null) {
                        _uiState.value = _uiState.value.copy(
                            status = LoadStatus.Error("Token exchange failed: ${exception.message}")
                        )
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        status = LoadStatus.Error("Authentication error: ${e.message}")
                    )
                    tokenProvider.clearTokens()
                }
            }
        }
    }

    fun resetStatus() {
        _uiState.value = _uiState.value.copy(status = LoadStatus.Init())
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.clearToken()
            _uiState.value = LoginUiState()
            authService?.dispose()
        }
    }

    override fun onCleared() {
        super.onCleared()
        authService?.dispose()
    }
}