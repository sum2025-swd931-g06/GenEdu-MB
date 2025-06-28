package com.example.mvvm.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mvvm.MainViewModel
import com.example.mvvm.Screen
import com.example.mvvm.ui.components.card.PremiumCard
import com.example.mvvm.ui.components.feature.FeatureRow
import com.example.mvvm.ui.components.header.HomeHeader
import com.example.mvvm.ui.components.recentproject.RecentProject
import com.example.mvvm.ui.components.text.HomeGreetingText
import com.example.mvvm.ui.screen.account.ProfileViewModel
import com.example.mvvm.utils.navigateTo
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
    mainViewModel: MainViewModel,
    profileViewModel: ProfileViewModel
//    projects: List<Project> = sampleProjects
) {

    val userData = mainViewModel.userData.collectAsState().value
    val username = userData?.name ?: "Guest"
    val uiState = viewModel.uiState.collectAsState().value
    val projects = uiState.projects

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF6366F1),
                                Color(0xFF8B5CF6),
                                Color(0xFFA855F7)
                            )
                        )
                    ),
                drawerContainerColor = Color.Transparent
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(320.dp)
                        .padding(20.dp)
                ) {
                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "GenEdu - Home",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.3f),
                                offset = Offset(2f, 2f),
                                blurRadius = 8f
                            )
                        )
                    )

                    Spacer(Modifier.height(32.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.15f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        // User profile section
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Profile",
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = username,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = userData?.email ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // My Projects
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Default.List,
                                contentDescription = "Projects",
                                tint = Color.White
                            )
                        },
                        label = {
                            Text(
                                "My Projects",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        selected = false,
                        onClick = {
                            navigateTo(navController, Screen.Project.route)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.White.copy(alpha = 0.1f),
                            selectedContainerColor = Color.White.copy(alpha = 0.2f)
                        )
                    )

                    Spacer(Modifier.weight(1f))

                    // Logout
                    NavigationDrawerItem(
                        icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Logout") },
                        label = {
                            Text(
                                "Logout",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        selected = false,
                        onClick = {
                            profileViewModel.logout { success ->
                                if (success) {
                                    // Clear user data in MainViewModel
                                    mainViewModel.setAuthenticated(false)
                                    mainViewModel.setUserData(null)

                                    // Navigate to intro screen
                                    navigateTo(navController, Screen.Intro.route)
                                }
                            }
                            navigateTo(navController, Screen.Intro.route)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.White.copy(alpha = 0.1f)
                        )
                    )

                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Enhanced Header
            HomeHeader(scope, drawerState, navController)

            Spacer(modifier = Modifier.height(12.dp))

            HomeGreetingText(username)

            Spacer(modifier = Modifier.height(16.dp))

            // Enhanced Premium Card
            PremiumCard()

            Spacer(modifier = Modifier.height(24.dp))

            // Features row
            FeatureRow()

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Projects
            RecentProject(navController, projects)
        }
    }
}