package com.example.mvvm.repositories

import android.util.Log
import com.example.mvvm.data.local.SharedPreferencesTokenProvider
import com.example.mvvm.models.FinalizedLecture
import com.example.mvvm.repositories.apis.project.FinalizedLectureApi
import com.example.mvvm.security.TokenProvider
import retrofit2.Response

class FinalizedLectureRepository(
    private val api: FinalizedLectureApi,
    private val tokenProvider: SharedPreferencesTokenProvider
) {
    suspend fun getFinalizedLectures(projectId: String): Response<List<FinalizedLecture>> {
        val token = tokenProvider.getToken()
        Log.d("FinalizedLectureRepo", "📦 Token from provider: ${token?.take(10) ?: "null"}")

        return if (token != null) {
            api.getFinalizedLectures(projectId, "Bearer $token")
        } else {
            throw IllegalStateException("❌ Token is null, cannot call API")
        }
    }

}
