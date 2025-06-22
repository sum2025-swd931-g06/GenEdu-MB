package com.example.mvvm.repositories.apis.project

import com.example.mvvm.configs.KeycloakAuthConfig
import com.example.mvvm.models.AudioProject
import com.example.mvvm.models.Project
import com.example.mvvm.models.ProjectData
import com.example.mvvm.models.ProjectStatus
import com.example.mvvm.models.UserData
import com.example.mvvm.repositories.SharedPreferencesTokenProvider
import com.example.mvvm.security.TokenProvider
import retrofit2.Response
import javax.inject.Inject

class ProjectRepository(
    private val api: ProjectApi,
    private val tokenProvider: TokenProvider
) {
    suspend fun getProjects(): Response<List<Project>> {
        val token = tokenProvider.getToken()
        return api.getProjectInfo("Bearer $token")
    }

}
