package com.example.mvvm.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.mvvm.models.UserData

object UserSessionUtils {
    
    fun saveUserSession(context: Context, userData: UserData) {
        val sharedPrefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putString("user_id", userData.id)
            .putString("user_name", userData.name)
            .putString("user_email", userData.email)
            .apply()
    }
    
    fun getUserId(context: Context): String? {
        val sharedPrefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPrefs.getString("user_id", null)
    }
    
    fun clearUserSession(context: Context) {
        val sharedPrefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .remove("user_id")
            .remove("user_name")
            .remove("user_email")
            .apply()
    }
}
