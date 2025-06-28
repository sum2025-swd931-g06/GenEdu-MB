package com.example.mvvm.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class NotificationItem(
    val id: Int,
    val type: String,
    val title: String,
    val description: String,
    val time: String,
    val isRead: Boolean,
    val icon: ImageVector,
    val iconColor: Color,
    val userId: String = "",
)