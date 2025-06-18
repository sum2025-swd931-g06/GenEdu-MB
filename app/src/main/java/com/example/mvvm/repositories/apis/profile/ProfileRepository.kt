package com.example.mvvm.repositories.apis.profile

import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApi: ProfileApi
) {
    suspend fun getProfiles(): List<UserProfile> {
        val response = profileApi.getProfiles()
        if (!response.isSuccessful) {
            throw Exception("Error: ${response.code()} ${response.message()}")
        }
        return response.body() ?: emptyList()
    }
}