package com.example.mvvm.ui.components.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mvvm.R
import com.example.mvvm.Screen
import com.example.mvvm.preview.EnhancedFeatureItem
import com.example.mvvm.utils.navigateTo

@Composable
fun FeatureRow(
    navController: NavHostController = rememberNavController()
){
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        EnhancedFeatureItem(
            iconRes = R.drawable.presentation_100,
            label = "Project",
            color = Color(0xFF3B82F6),
            navigateTo = { navigateTo(navController, Screen.Project.route) }
        )
        EnhancedFeatureItem(
            iconRes = R.drawable.audio_100,
            label = "Audio",
            color = Color(0xFF10B981),
            navigateTo = { navigateTo(navController, Screen.Project.route) }
        )
        EnhancedFeatureItem(
            iconRes = R.drawable.profile_100,
            label = "Profile",
            color = Color(0xFFF59E0B),
            navigateTo = { navigateTo(navController, Screen.UserProfile.route) }
        )
    }
}