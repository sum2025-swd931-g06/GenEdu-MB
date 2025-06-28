package com.example.mvvm.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mvvm.MainViewModel
import com.example.mvvm.R
import com.example.mvvm.Screen
import com.example.mvvm.preview.EnhancedButton
import com.example.mvvm.preview.EnhancedFeatureItem
import com.example.mvvm.preview.EnhancedIconButton
import com.example.mvvm.preview.EnhancedProjectCard
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
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Hi $username 👋",
                style = TextStyle(
                    fontSize = 18.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = "Manage your project",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B),
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.1f),
                        offset = Offset(1f, 1f),
                        blurRadius = 4f
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Enhanced Premium Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 16.dp,
                    pressedElevation = 20.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF8B5CF6),
                                    Color(0xFFA855F7),
                                    Color(0xFFEC4899)
                                )
                            )
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.earth_100),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .drawBehind {
                                    drawCircle(
                                        color = Color.White.copy(alpha = 0.2f),
                                        radius = size.width / 2 + 8.dp.toPx()
                                    )
                                }
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Column {
                            Text(
                                text = "Unlimited Storage",
                                fontSize = 16.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "$30/year",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.Black.copy(alpha = 0.3f),
                                        offset = Offset(2f, 2f),
                                        blurRadius = 4f
                                    )
                                )
                            )
                            Text(
                                text = "Offer till May 26",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            EnhancedButton(
                                text = "Upgrade Now",
                                onClick = {}
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Features row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
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

            Spacer(modifier = Modifier.height(24.dp))

            // Recents
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

            // Project list
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
    }
}