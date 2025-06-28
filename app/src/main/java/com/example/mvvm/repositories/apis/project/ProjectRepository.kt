package com.example.mvvm.repositories.apis.project

import com.example.mvvm.models.Project
import com.example.mvvm.security.TokenProvider
import retrofit2.Response

class ProjectRepository(
    private val api: ProjectApi,
    private val tokenProvider: TokenProvider
) {
    suspend fun getProjects(): Response<List<Project>> {
        val token = tokenProvider.getToken()
        return api.getProjectInfo("Bearer $token")
    }

}
