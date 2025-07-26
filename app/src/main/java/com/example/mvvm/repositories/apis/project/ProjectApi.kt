package com.example.mvvm.repositories.apis.project

import com.example.mvvm.models.Project
import com.example.mvvm.ui.screen.project.ProjectResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ProjectApi {
//    @GET("/present")
    @GET("projects")
    suspend fun getProjectInfo(
        @Header("Authorization") authorization: String
    ): Response<ProjectResponse>


}