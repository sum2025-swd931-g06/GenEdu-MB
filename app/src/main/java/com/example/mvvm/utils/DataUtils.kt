package com.example.mvvm.utils

import com.example.mvvm.mock.sampleProjects
import com.example.mvvm.models.Project
import com.example.mvvm.models.ProjectStatus
import com.example.mvvm.models.UserData

fun findProjectById(projectId: String): Project {
    // In a real app, you would fetch this from ViewModel or repository
    return sampleProjects.find { it.id == projectId } ?: Project(
        id = projectId,
        title = "Project not found",
        status = ProjectStatus.DRAFT,
        creationTime = System.currentTimeMillis(),
        slideNum = 0
    )
}

suspend fun fetchUserInfo(accessToken: String): UserData {
    // Here you would make an API call to your auth server
    // For this example, we'll create dummy data based on the token
    return UserData(
        id = "user123",
        name = "John Doe",
        email = "john.doe@example.com",
        idNumber = ""
    )
}