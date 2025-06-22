package com.example.mvvm.repositories.apis.project

import com.example.mvvm.models.AudioProject
import com.example.mvvm.models.ProjectStatus
import com.example.mvvm.models.TokenIntrospectionResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import com.example.mvvm.models.Project
import retrofit2.http.POST

interface ProjectApi {
    @GET("/present")
    suspend fun getProjectInfo(
        @Header("Authorization") authorization: String
    ): Response<List<Project>>

}