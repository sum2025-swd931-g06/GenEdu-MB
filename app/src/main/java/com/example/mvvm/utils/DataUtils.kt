package com.example.mvvm.utils

import com.example.mvvm.mock.sampleProjects
import com.example.mvvm.models.Project
import com.example.mvvm.models.ProjectStatus

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