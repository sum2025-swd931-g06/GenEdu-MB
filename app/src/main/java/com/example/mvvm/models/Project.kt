package com.example.mvvm.models

data class Project(
    val id: String,
    val title: String,
    val status: ProjectStatus,
    val creationTime: Long,
    val slideNum: Int = 0,
    val audioProject: AudioProject
){

}

enum class ProjectStatus {
    DRAFT, IN_PROGRESS, COMPLETED
}