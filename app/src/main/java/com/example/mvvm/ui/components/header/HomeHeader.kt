package com.example.mvvm.ui.components.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.mvvm.Screen
import com.example.mvvm.preview.EnhancedIconButton
import com.example.mvvm.utils.navigateTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeHeader(
    scope: CoroutineScope = rememberCoroutineScope(),
    drawerState: androidx.compose.material3.DrawerState,
    navController: androidx.navigation.NavHostController
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        EnhancedIconButton(
            icon = Icons.Default.Menu,
            contentDescription = "Menu",
            onClick = { scope.launch { drawerState.open() } }
        )
        EnhancedIconButton(
            icon = Icons.Default.NotificationsActive,
            contentDescription = "notifications",
            onClick = {
                navigateTo(navController, Screen.Notification.route)
            }
        )
    }
}