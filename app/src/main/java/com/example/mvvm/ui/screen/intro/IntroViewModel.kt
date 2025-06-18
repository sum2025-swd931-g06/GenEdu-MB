package com.example.mvvm.ui.screen.intro

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
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
import net.openid.appauth.ClientSecretPost
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
        viewModelScope.launch {
            try {
                Log.d("IntroViewModel", "Handling auth result")
                _uiState.value = _uiState.value.copy(status = LoadStatus.Loading())

                val response = intent?.let { AuthorizationResponse.fromIntent(it) }
                val exception = AuthorizationException.fromIntent(intent)

                when {
                    response != null -> {
                        Log.d("IntroViewModel", "Got auth response, exchanging tokens")
                        Log.d("IntroViewModel", "Response: ${response.toString()}")
                        exchangeTokens(response)
                    }
                    exception != null -> {
                        Log.e("IntroViewModel", "Auth failed: ${exception.message}")
                        _uiState.value = _uiState.value.copy(
                            status = LoadStatus.Error("Authorization failed: ${exception.message}")
                        )
                    }
                    else -> {
                        Log.e("IntroViewModel", "No response received")
                        _uiState.value = _uiState.value.copy(
                            status = LoadStatus.Error("No response received")
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("IntroViewModel", "Auth error: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    status = LoadStatus.Error("Authentication error: ${e.message}")
                )
            }
        }
    }

    private fun exchangeTokens(authResponse: AuthorizationResponse) {
        val tokenRequest = authResponse.createTokenExchangeRequest()
        Log.d("IntroViewModel", "Starting token exchange with:")
        Log.d("IntroViewModel", "Client ID: ${KeycloakAuthConfig.CLIENT_ID}")
        Log.d("IntroViewModel", "Token endpoint: ${KeycloakAuthConfig.TOKEN_ENDPOINT}")

        // Add client authentication with secret
        val clientAuth = ClientSecretPost(KeycloakAuthConfig.CLIENT_SECRET)

        authService?.performTokenRequest(
            tokenRequest,
            clientAuth  // This is the missing piece!
        ) { tokenResponse, exception ->
            viewModelScope.launch {
                try {
                    if (tokenResponse != null) {
                        Log.d("IntroViewModel", "Token response received")
                        val accessToken = tokenResponse.accessToken
                        val refreshToken = tokenResponse.refreshToken

                        if (accessToken != null) {
                            // Store tokens synchronously
                            tokenProvider.saveToken(accessToken)

                            // Verify token was stored
                            val storedToken = tokenProvider.getToken()
                            Log.d("IntroViewModel", "Token storage verified: ${storedToken != null}")

                            if (tokenResponse.refreshToken != null) {
                                tokenProvider.saveRefreshToken(refreshToken ?: "Not Available")
                            }

                            // Token introspection
                            withTimeout(5000) {
                                keycloakRepository.introspectToken(accessToken).fold(
                                    onSuccess = { userData ->
                                        Log.d("IntroViewModel", "Token introspection successful")
                                        _uiState.value = _uiState.value.copy(
                                            isAuthenticated = true,
                                            accessToken = accessToken,
                                            userData = userData,
                                            status = LoadStatus.Success()
                                        )
                                    },
                                    onFailure = { error ->
                                        Log.e("IntroViewModel", "Token introspection failed: ${error.message}")
                                        _uiState.value = _uiState.value.copy(
                                            status = LoadStatus.Error("Token validation failed: ${error.message}")
                                        )
                                        tokenProvider.clearTokens()
                                    }
                                )
                            }
                        }
                    } else if (exception != null) {
                        Log.e("IntroViewModel", "Token exchange failed: ${exception.message}")
                        Log.e("IntroViewModel", "Error details: ${exception.error}, ${exception.errorDescription}")
                        _uiState.value = _uiState.value.copy(
                            status = LoadStatus.Error("Token exchange failed: ${exception.errorDescription}")
                        )
                    }
                } catch (e: Exception) {
                    Log.e("IntroViewModel", "Exchange error: ${e.message}")
                    _uiState.value = _uiState.value.copy(
                        status = LoadStatus.Error("Authentication error: ${e.message}")
                    )
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