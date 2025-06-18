package com.example.mvvm.security

interface TokenProvider {
    fun getToken(): String?
    fun saveToken(token: String)
    fun getRefreshToken(): String?
    fun saveRefreshToken(refreshToken: String)
    fun clearTokens()
}
