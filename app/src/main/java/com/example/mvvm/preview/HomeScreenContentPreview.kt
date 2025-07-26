package com.example.mvvm.preview

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.mvvm.R
import com.example.mvvm.Screen
import com.example.mvvm.mock.sampleProjects
import com.example.mvvm.models.AudioProject
import com.example.mvvm.models.AudioProjectStatus
import com.example.mvvm.models.Project
import com.example.mvvm.models.ProjectStatus
import com.example.mvvm.ui.components.cards.ProjectCard
import com.example.mvvm.ui.components.feature.FeatureRow
import com.example.mvvm.ui.components.featureitems.FeatureItem
import kotlinx.coroutines.launch

@Composable
fun HomeScreenPreviewContent(
    username: String = "Guest",
    email: String = "",
    projects: List<Project> = emptyList(),
    onNavigate: (String) -> Unit = {},
    onLogout: () -> Unit = {}
) {
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
                        text = "My MVVM App",
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

                    // Enhanced Profile Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.15f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.2f),
                                        CircleShape
                                    )
                                    .drawBehind {
                                        drawCircle(
                                            color = Color.White.copy(alpha = 0.1f),
                                            radius = size.width / 2 + 8.dp.toPx()
                                        )
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profile",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = username,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = email,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Enhanced Navigation Items
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
                            onNavigate(Screen.Project.route)
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

                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                Icons.Default.ExitToApp,
                                contentDescription = "Logout",
                                tint = Color.White
                            )
                        },
                        label = {
                            Text(
                                "Logout",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        selected = false,
                        onClick = {
                            onLogout()
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
        // Main Content with Gradient Background
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp)
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

                Spacer(modifier = Modifier.height(24.dp))

                // Enhanced Welcome Text
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

                Spacer(modifier = Modifier.height(24.dp))

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

                Spacer(modifier = Modifier.height(32.dp))

                // Enhanced Feature Items
                FeatureRow(rememberNavController())

                Spacer(modifier = Modifier.height(32.dp))

                // Enhanced Section Header
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
                            onNavigate(Screen.Project.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Enhanced Project Cards
                projects.forEach { project ->
                    EnhancedProjectCard(
                        project = project,
                        onClick = {
                            onNavigate(Screen.ProjectDetail.createRoute(project.id))
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun EnhancedIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100)
    )

    Box(
        modifier = Modifier
            .size(48.dp)
            .scale(scale)
            .background(
                Color.White,
                CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, radius = 24.dp),
                onClick = onClick
            )
            .drawBehind {
                drawCircle(
                    color = Color.Black.copy(alpha = 0.1f),
                    radius = size.width / 2,
                    center = center + Offset(2.dp.toPx(), 2.dp.toPx())
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color(0xFF64748B),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun EnhancedButton(
    text: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100)
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .scale(scale)
            .drawBehind {
                drawRoundRect(
                    color = Color.Black.copy(alpha = 0.2f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx()),
                    topLeft = Offset(4.dp.toPx(), 4.dp.toPx()),
                    size = androidx.compose.ui.geometry.Size(
                        size.width,
                        size.height
                    )
                )
            },
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 4.dp
        )
    ) {
        Text(
            text,
            color = Color(0xFF8B5CF6),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun EnhancedFeatureItem(
    iconRes: Int,
    label: String,
    color: Color,
    navigateTo: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(150)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = navigateTo
            )
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    color.copy(alpha = 0.1f),
                    RoundedCornerShape(20.dp)
                )
                .drawBehind {
                    // Glow effect
                    drawRoundRect(
                        color = color.copy(alpha = 0.3f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(20.dp.toPx()),
                        topLeft = Offset(-4.dp.toPx(), -4.dp.toPx()),
                        size = androidx.compose.ui.geometry.Size(
                            size.width + 8.dp.toPx(),
                            size.height + 8.dp.toPx()
                        )
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = label,
                modifier = Modifier.size(50.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF475569)
        )
    }
}

@Composable
fun EnhancedProjectCard(
    project: Project,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(150)
    )

    Column(modifier = Modifier.padding(start = 10.dp, end = 10.dp)) {
        ProjectCard(
            navController = rememberNavController(),
            project = project,
            onClick = onClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val fakeProjects = listOf(
        Project(
            id = "2",
            title = "Hóa học cơ bản - Chương 3",
            status = ProjectStatus.IN_PROGRESS,
//            creationTime = System.currentTimeMillis() - 86400000,
            slideNum = 15,
            audioProject = AudioProject(
                id = "2",
                title = "Hóa học cơ bản - Audio",
                status = AudioProjectStatus.PROCESSING,
                creationTime = System.currentTimeMillis() - 86400000,
                durationSeconds = 142,
                textContent = "Các phản ứng hóa học cơ bản và ứng dụng trong đời sống.",
                audioUrl = "https://example.com/audio/234567.mp3",
                voiceType = "Nam miền Nam"
            )
        ),
        Project(
            id = "3",
            title = "Toán học nâng cao - Đại số",
            status = ProjectStatus.DRAFT,
//            creationTime = System.currentTimeMillis() - 86400000 * 3,
            slideNum = 30,
            audioProject = null
        ),
        Project(
            id = "4",
            title = "Văn học dân gian",
            status = ProjectStatus.IN_PROGRESS,
//            creationTime = System.currentTimeMillis() - 86400000 * 4,
            slideNum = 12,
            audioProject = AudioProject(
                id = "4",
                title = "Truyện cổ tích Việt Nam",
                status = AudioProjectStatus.PROCESSING,
                creationTime = System.currentTimeMillis() - 86400000 * 4,
                durationSeconds = 220,
                textContent = "Ngày xửa ngày xưa...",
                audioUrl = "https://example.com/audio/456789.mp3",
                voiceType = "Nữ miền Trung"
            )
        ),
    )

    HomeScreenPreviewContent(
        username = "Hoàng",
        email = "hoang@example.com",
        projects = fakeProjects,
        onNavigate = { println("Navigate to $it") },
        onLogout = { println("Logout clicked") }
    )
}

@Preview(showBackground = true)
@Composable
fun ProjectCardPreview() {
    val navController = rememberNavController()
    Column(modifier = Modifier.padding(16.dp)) {
        ProjectCard(
            navController = navController,
            project = sampleProjects.first()
        )
        ProjectCard(
            navController = navController,
            project = sampleProjects[1]
        )
    }
}