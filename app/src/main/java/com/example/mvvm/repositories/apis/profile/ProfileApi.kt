package com.example.mvvm.repositories.apis.profile

import retrofit2.Response
import retrofit2.http.GET

interface ProfileApi {
    @GET("v3/2d5bc47d-d072-46ab-b932-49f9d1977c54")
    suspend fun getProfiles(): Response<List<UserProfile>>
}

data class UserProfile(
    val id: Int,
    val name: String,
    val email: String,
)