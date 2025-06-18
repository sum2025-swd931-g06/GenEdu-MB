package com.example.mvvm.repositories.apis.keycloak

import com.example.mvvm.configs.KeycloakAuthConfig
import com.example.mvvm.models.UserData
import com.example.mvvm.repositories.SharedPreferencesTokenProvider
import javax.inject.Inject

class KeycloakRepository @Inject constructor(
    private val keycloakApi: KeycloakApi,
    private val tokenProvider: SharedPreferencesTokenProvider
) {
    suspend fun introspectToken(token: String): Result<UserData> {
        return try {
            val response = keycloakApi.introspectToken(
                token,
                KeycloakAuthConfig.CLIENT_ID,
                KeycloakAuthConfig.CLIENT_SECRET
            )

            if (!response.isSuccessful) {
                return Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }

            val introspection = response.body()
            if (introspection == null || !introspection.active) {
                return Result.failure(Exception("Token is invalid or inactive"))
            }

            Result.success(UserData(
                id = introspection.sub,
                name = introspection.name,
                email = introspection.email,
                idNumber = ""
            ))
        } catch (e: Exception) {
            Result.failure(Exception("Token introspection failed: ${e.message}", e))
        }
    }

    suspend fun logout(refreshToken: String): Result<Unit> {
        return try {
            val response = keycloakApi.logout(
                refreshToken,
                KeycloakAuthConfig.CLIENT_ID,
                KeycloakAuthConfig.CLIENT_SECRET
            )

            if (!response.isSuccessful) {
                return Result.failure(Exception("Logout failed: ${response.code()} ${response.message()}"))
            }

            // Clear local tokens
            tokenProvider.clearTokens()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Logout error: ${e.message}", e))
        }
    }
}