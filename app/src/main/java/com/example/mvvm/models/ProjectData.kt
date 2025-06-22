package com.example.mvvm.models

data class ProjectData(
    val id: String = "",
    val title: String = "",
    val status: ProjectStatus = ProjectStatus.DRAFT,
    val creationTime: Long = 9876,
    val slideNum: Int = 1,
    val audioProject: AudioProject? = null
)