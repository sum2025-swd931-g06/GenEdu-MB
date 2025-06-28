package com.example.mvvm.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.Color
import com.example.mvvm.mock.sampleProjects
import com.example.mvvm.models.NotificationItem
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

val sampleNotificationData = listOf(
    NotificationItem(
        id = 1,
        type = "message",
        title = "New message from Sarah",
        description = "Hey! Are we still meeting for coffee tomorrow?",
        time = "2 min ago",
        isRead = false,
        icon = Icons.Default.Message,
        iconColor = Color(0xFF2196F3)
    ),
    NotificationItem(
        id = 2,
        type = "like",
        title = "Someone liked your photo",
        description = "Your recent post got 15 new likes",
        time = "1 hour ago",
        isRead = false,
        icon = Icons.Default.Favorite,
        iconColor = Color(0xFFE91E63)
    ),
    NotificationItem(
        id = 3,
        type = "order",
        title = "Order confirmed",
        description = "Your order #12345 has been confirmed and will be delivered tomorrow",
        time = "3 hours ago",
        isRead = true,
        icon = Icons.Default.ShoppingCart,
        iconColor = Color(0xFF4CAF50)
    ),
    NotificationItem(
        id = 4,
        type = "friend",
        title = "New follower",
        description = "Alex started following you",
        time = "1 day ago",
        isRead = true,
        icon = Icons.Default.Person,
        iconColor = Color(0xFF9C27B0)
    ),
    NotificationItem(
        id = 5,
        type = "system",
        title = "App update available",
        description = "Version 2.1.0 includes new features and bug fixes",
        time = "2 days ago",
        isRead = true,
        icon = Icons.Default.Settings,
        iconColor = Color(0xFF607D8B)
    ),
    NotificationItem(
        id = 6,
        type = "reminder",
        title = "Meeting reminder",
        description = "Don't forget your team meeting at 3 PM today",
        time = "30 min ago",
        isRead = false,
        icon = Icons.Default.Schedule,
        iconColor = Color(0xFFFF9800)
    )
)