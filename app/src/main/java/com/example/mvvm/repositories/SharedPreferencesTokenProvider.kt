package com.example.mvvm.repositories

import android.content.SharedPreferences
import android.util.Log
import com.example.mvvm.security.TokenProvider
import javax.inject.Inject

class SharedPreferencesTokenProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TokenProvider {

    override fun getToken(): String? {
        val token = sharedPreferences.getString("access_token", null)
        Log.d("TokenProvider", "Retrieved token: ${token?.take(10)}...")
        return token
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, token).commit()
        Log.d("TokenProvider", "Access token saved")
    }

    override fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }

    override fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit().putString(REFRESH_TOKEN_KEY, refreshToken).commit()
    }

    override fun clearTokens() {
        sharedPreferences.edit()
            .remove(ACCESS_TOKEN_KEY)
            .remove(REFRESH_TOKEN_KEY)
            .apply()
    }

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }
}