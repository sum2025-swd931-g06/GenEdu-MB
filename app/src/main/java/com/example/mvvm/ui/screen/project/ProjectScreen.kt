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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.mvvm.models.AudioProject
import com.example.mvvm.models.AudioProjectStatus
import com.example.mvvm.models.Project
import com.example.mvvm.models.ProjectStatus
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

@Composable
fun ViewToggle(
    selectedMode: ViewMode,
    onModeChange: (ViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(25.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        ViewMode.values().forEach { mode ->
            val isSelected = selectedMode == mode
            val backgroundColor = if (isSelected) Color.White else Color.Transparent
            val textColor = if (isSelected) Color(0xFF2196F3) else Color.White

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(backgroundColor)
                    .clickable { onModeChange(mode) }
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (mode) {
                        ViewMode.PROJECTS -> "Dự án"
                        ViewMode.AUDIO -> "Audio"
                    },
                    color = textColor,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun StorageIndicator(
    usedStorage: Float,
    totalStorage: Float,
    modifier: Modifier = Modifier
) {
    val usagePercentage = (usedStorage / totalStorage).coerceIn(0f, 1f)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = Color(0xFF4CAF50),
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Dung lượng đã sử dụng",
                    fontSize = 12.sp,
                    color = Color(0xFF757575)
                )
            }
            Text(
                text = "${String.format("%.1f", usedStorage)}GB / ${String.format("%.1f", totalStorage)}GB",
                fontSize = 12.sp,
                color = Color(0xFF212121),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = usagePercentage,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = when {
                usagePercentage < 0.7f -> Color(0xFF4CAF50)
                usagePercentage < 0.9f -> Color(0xFFFF9800)
                else -> Color(0xFFF44336)
            },
            trackColor = Color(0xFFE0E0E0)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${String.format("%.1f", totalStorage - usedStorage)}GB còn lại",
            fontSize = 10.sp,
            color = Color(0xFF9E9E9E)
        )
    }
}

@Composable
fun AudioProjectStatusChip(status: AudioProjectStatus) {
    val (backgroundColor, textColor) = when (status) {
        AudioProjectStatus.DRAFT -> Color(0xFFE0E0E0) to Color(0xFF616161)
        AudioProjectStatus.PROCESSING -> Color(0xFFFFF9C4) to Color(0xFFFBC02D)
        AudioProjectStatus.COMPLETED -> Color(0xFFE8F5E9) to Color(0xFF4CAF50)
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = status.name.replace("_", " "),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
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

@Composable
fun ProjectStatusChip(status: ProjectStatus) {
    val (backgroundColor, textColor) = when (status) {
        ProjectStatus.DRAFT -> Color(0xFFE0E0E0) to Color(0xFF616161)
        ProjectStatus.IN_PROGRESS -> Color(0xFFFFF9C4) to Color(0xFFFBC02D)
        ProjectStatus.COMPLETED -> Color(0xFFE8F5E9) to Color(0xFF4CAF50)
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = status.name.replace("_", " "),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF00BCD4), // Cyan
                        Color(0xFF2196F3), // Blue
                        Color(0xFF1976D2)  // Dark Blue
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

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return localDateTime.format(formatter)
}

private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%d:%02d", minutes, remainingSeconds)
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ProjectScreenPreview() {
    MaterialTheme {
        ProjectScreen()
    }
}