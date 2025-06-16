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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
    onCreateNewProject: () -> Unit = {}
) {
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
            id = "3",
            title = "Lịch sử Việt Nam thời kỳ đổi mới",
            status = ProjectStatus.DRAFT,
            creationTime = System.currentTimeMillis(),
            slideNum = 8,
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
        )
    )

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
            Spacer(modifier = Modifier.height(24.dp))

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
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(projects) { project ->
                            ProjectItem(
                                project = project,
                                onClick = onProjectClick
                            )
                        }
                    }

                    // Floating Action Button for creating new project
                    FloatingActionButton(
                        onClick = onCreateNewProject,
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomEnd),
                        containerColor = Color(0xFF2196F3),
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create New Project"
                        )
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
        ProjectScreen()
    }
}