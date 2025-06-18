package com.example.mvvm.repositories

import android.content.SharedPreferences
import com.example.mvvm.constant.AuthConstants
import com.example.mvvm.security.TokenProvider
import javax.inject.Inject

class SharedPreferencesTokenProvider @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TokenProvider {
    override fun getToken(): String? {
        return sharedPreferences.getString(AuthConstants.TOKEN_KEY, null)
    }
}
