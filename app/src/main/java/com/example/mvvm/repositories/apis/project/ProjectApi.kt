package com.example.mvvm.repositories.apis.project

import com.example.mvvm.models.Project
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ProjectApi {
    @GET("/present")
    suspend fun getProjectInfo(
        @Header("Authorization") authorization: String
    ): Response<List<Project>>

}