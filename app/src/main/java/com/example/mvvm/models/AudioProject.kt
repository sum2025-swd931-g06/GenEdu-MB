package com.example.mvvm.models

data class AudioProject(
    val id: String,
    val title: String,
    val status: AudioProjectStatus,
    val creationTime: Long,
    val durationSeconds: Int = 0,
    val textContent: String = "",
    val audioUrl: String? = null,
    val voiceType: String = "Default"
)

enum class AudioProjectStatus {
    DRAFT, PROCESSING, COMPLETED
}
