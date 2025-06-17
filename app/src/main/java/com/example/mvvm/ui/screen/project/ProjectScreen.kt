package com.example.mvvm.ui.screen.project

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mvvm.models.AudioProject
import com.example.mvvm.models.AudioProjectStatus
import com.example.mvvm.models.Project
import com.example.mvvm.models.ProjectStatus
import com.example.mvvm.ui.components.StorageIndicator
import com.example.mvvm.ui.components.ViewToggle
import com.example.mvvm.ui.components.chips.AudioProjectStatusChip
import com.example.mvvm.ui.components.chips.ProjectStatusChip
import com.example.mvvm.ui.theme.DarkPurple
import com.example.mvvm.ui.theme.LightPurple
import com.example.mvvm.ui.theme.MainColor
import com.example.mvvm.utils.formatDuration
import com.example.mvvm.utils.navigateToHome
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.Calendar
import compose.icons.fontawesomeicons.regular.FileArchive
import compose.icons.fontawesomeicons.regular.FileAudio
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

enum class ViewMode {
    PROJECTS, AUDIO
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AudioProjectItem(
    audioProject: AudioProject,
    onClick: (AudioProject) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(audioProject) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Audio icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Regular.FileAudio,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = audioProject.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF212121),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AudioProjectStatusChip(audioProject.status)

                    Text(
                        text = "${formatDuration(audioProject.durationSeconds)}",
                        fontSize = 12.sp,
                        color = Color(0xFF757575)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = FontAwesomeIcons.Regular.Calendar,
                        contentDescription = null,
                        tint = Color(0xFF9E9E9E),
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    val formattedDate = formatDate(audioProject.creationTime)
                    Text(
                        text = formattedDate,
                        fontSize = 12.sp,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }

            // Play button
            if (audioProject.status == AudioProjectStatus.COMPLETED) {
                IconButton(
                    onClick = { /* Handle play audio */ },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play Audio",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProjectItem(
    project: Project,
    onClick: (Project) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(project) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Project icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE3F2FD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Regular.FileArchive,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF212121),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProjectStatusChip(project.status)

                    Text(
                        text = "${project.slideNum} slides",
                        fontSize = 12.sp,
                        color = Color(0xFF757575)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = FontAwesomeIcons.Regular.Calendar,
                        contentDescription = null,
                        tint = Color(0xFF9E9E9E),
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    val formattedDate = formatDate(project.creationTime)
                    Text(
                        text = formattedDate,
                        fontSize = 12.sp,
                        color = Color(0xFF9E9E9E)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProjectScreen(
    navController: NavController,
    onBackClick: () -> Unit = {},
    onProjectClick: (Project) -> Unit = {},
    onAudioProjectClick: (AudioProject) -> Unit = {},
    onCreateNewProject: () -> Unit = {}
) {
    var selectedMode by remember { mutableStateOf(ViewMode.PROJECTS) }

    // Sample projects for preview
    val projects = listOf(
        Project(
            id = "1",
            title = "Bài giảng Sinh học lớp 10",
            status = ProjectStatus.COMPLETED,
            creationTime = System.currentTimeMillis() - 86400000 * 2,
            slideNum = 24,
            audioProject = AudioProject(
                id = "1",
                title = "Bài văn Tiếng Việt lớp 10",
                status = AudioProjectStatus.COMPLETED,
                creationTime = System.currentTimeMillis() - 86400000 * 2,
                durationSeconds = 187,
                textContent = "Việt Nam là một quốc gia nằm ở khu vực Đông Nam Á. Việt Nam có nhiều danh lam thắng cảnh và nhiều di sản văn hóa thế giới được UNESCO công nhận.",
                audioUrl = "https://example.com/audio/123456.mp3",
                voiceType = "Nữ miền Bắc"
            )
        ),
        Project(
            id = "2",
            title = "Hóa học cơ bản - Chương 3",
            status = ProjectStatus.IN_PROGRESS,
            creationTime = System.currentTimeMillis() - 86400000,
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
            title = "Lịch sử Việt Nam thời kỳ đổi mới",
            status = ProjectStatus.DRAFT,
            creationTime = System.currentTimeMillis(),
            slideNum = 8,
            audioProject = AudioProject(
                id = "3",
                title = "Lịch sử Việt Nam - Đổi mới",
                status = AudioProjectStatus.DRAFT,
                creationTime = System.currentTimeMillis(),
                durationSeconds = 0,
                textContent = "Thời kỳ đổi mới của Việt Nam từ năm 1986 đến nay.",
                audioUrl = "",
                voiceType = "Nữ miền Trung"
            )
        )
    )

    val audioProjects = projects.mapNotNull { it.audioProject }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            LightPurple,
                            MainColor,
                            DarkPurple
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Status bar spacer
                Spacer(modifier = Modifier.height(5.dp))

                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            modifier = Modifier.clickable {
                                navigateToHome(navController)
                            },
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "Dự án của tôi",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(48.dp))
                }

                // View Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ViewToggle(
                        selectedMode = selectedMode,
                        onModeChange = { selectedMode = it }
                    )
                }

                // Content Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Color(0xFFF5F5F5),
                    shadowElevation = 8.dp
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            // Storage Indicator
                            StorageIndicator(
                                usedStorage = 2.3f,
                                totalStorage = 5.0f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            )

                            // Content List
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 80.dp)
                            ) {
                                when (selectedMode) {
                                    ViewMode.PROJECTS -> {
                                        items(projects) { project ->
                                            ProjectItem(
                                                project = project,
                                                onClick = onProjectClick
                                            )
                                        }
                                    }

                                    ViewMode.AUDIO -> {
                                        items(audioProjects) { audioProject ->
                                            AudioProjectItem(
                                                audioProject = audioProject,
                                                onClick = onAudioProjectClick
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return localDateTime.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ProjectScreenPreview() {
    MaterialTheme {
        ProjectScreen(rememberNavController())
    }
}