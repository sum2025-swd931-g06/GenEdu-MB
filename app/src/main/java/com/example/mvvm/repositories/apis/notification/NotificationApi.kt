package com.example.mvvm.repositories.apis.notification

import com.example.mvvm.models.NotificationItem
import retrofit2.Response
import retrofit2.http.GET

interface NotificationApi {
    @GET("/notifications")
    suspend fun getNotifications(): Response<List<NotificationItem>>




}