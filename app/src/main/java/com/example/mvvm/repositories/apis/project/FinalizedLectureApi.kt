package com.example.mvvm.repositories.apis.project

import com.example.mvvm.models.FinalizedLecture
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface FinalizedLectureApi {
    @GET("projects/{projectId}/finalized-lectures")
    suspend fun getFinalizedLectures(
        @Path("projectId") projectId: String,
        @Header("Authorization") token: String
    ): Response<List<FinalizedLecture>>
}
