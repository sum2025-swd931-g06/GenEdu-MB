package com.example.mvvm.ui.components.recentproject

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mvvm.Screen
import com.example.mvvm.models.Project
import com.example.mvvm.preview.EnhancedProjectCard
import com.example.mvvm.utils.navigateTo

@Composable
fun RecentProject(
    navController: NavController = rememberNavController(),
    projects: List<Project>,
    isLoading: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Recent Projects",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF1E293B)
            )
        )
        Text(
            "View all",
            style = TextStyle(
                color = Color(0xFF8B5CF6),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.clickable {
                navigateTo(navController, Screen.Project.route)
            }
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // ✅ Hiển thị loading
    if (isLoading) {
        androidx.compose.material3.CircularProgressIndicator()
        return // không hiển thị list khi đang loading
    }

    // ✅ Hiển thị danh sách project
    projects.forEach { project ->
        EnhancedProjectCard(
            project = project,
            onClick = {
                navigateTo(
                    navController,
                    Screen.ProjectDetail.createRoute(project.id)
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}