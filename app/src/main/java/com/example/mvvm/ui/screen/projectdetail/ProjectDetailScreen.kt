package com.example.mvvm.ui.screen.projectdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvvm.models.AudioProject
import com.example.mvvm.models.AudioProjectStatus
import com.example.mvvm.models.Project
import com.example.mvvm.models.ProjectStatus
import com.example.mvvm.ui.screen.project.ProjectStatusChip
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.Calendar
import compose.icons.fontawesomeicons.regular.FileArchive
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProjectDetailScreen(
    project: Project = Project(
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

    onBackClick: () -> Unit = {},
    onShareClick: (Project) -> Unit = {},
    onPlayAudio: (String) -> Unit = {}
) {

    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0) }

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
                    text = "Chi tiết dự án",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                IconButton(onClick = { onShareClick(project) }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.White
                    )
                }
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
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Project Header
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Project icon
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE3F2FD)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = FontAwesomeIcons.Regular.FileArchive,
                                        contentDescription = null,
                                        tint = Color(0xFF2196F3),
                                        modifier = Modifier.size(36.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = project.title,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF212121),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ProjectStatusChip(project.status)

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = "${project.slideNum} slides",
                                        fontSize = 14.sp,
                                        color = Color(0xFF757575)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = FontAwesomeIcons.Regular.Calendar,
                                        contentDescription = null,
                                        tint = Color(0xFF9E9E9E),
                                        modifier = Modifier.size(16.dp)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    val formattedDate = formatDate(project.creationTime)
                                    Text(
                                        text = "Tạo ngày $formattedDate",
                                        fontSize = 14.sp,
                                        color = Color(0xFF9E9E9E)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Project Details
                        Text(
                            text = "Slides",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Slides list or preview
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                repeat(minOf(3, project.slideNum)) { index ->
                                    SlidePreviewItem(
                                        slideNumber = index + 1,
                                        title = "Slide ${index + 1}"
                                    )

                                    if (index < minOf(2, project.slideNum - 1)) {
                                        Divider(
                                            modifier = Modifier.padding(vertical = 12.dp),
                                            color = Color(0xFFEEEEEE)
                                        )
                                    }
                                }

                                if (project.slideNum > 3) {
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = "Xem tất cả ${project.slideNum} slides",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        textAlign = TextAlign.Center,
                                        color = Color(0xFF2196F3),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Audio Player
                        if (project.audioProject.status == AudioProjectStatus.COMPLETED && project.audioProject.audioUrl != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Audio Player",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF212121)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Audio control buttons
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(onClick = { /* Rewind logic */ }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowBack,
                                                contentDescription = "Rewind 10 seconds",
                                                tint = Color(0xFF2196F3)
                                            )
                                        }

                                        FloatingActionButton(
                                            onClick = {
                                                isPlaying = !isPlaying
                                                if (isPlaying) {
                                                    onPlayAudio(project.audioProject.audioUrl)
                                                }
                                            },
                                            containerColor = Color(0xFF2196F3),
                                            contentColor = Color.White,
                                            modifier = Modifier.size(56.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isPlaying) Icons.Default.Close else Icons.Default.PlayArrow,
                                                contentDescription = if (isPlaying) "Pause" else "Play",
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }

                                        IconButton(onClick = { /* Fast forward logic */ }) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Forward 10 seconds",
                                                tint = Color(0xFF2196F3)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Progress bar
                                    Slider(
                                        value = currentPosition.toFloat(),
                                        onValueChange = { currentPosition = it.toInt() },
                                        valueRange = 0f..project.audioProject.durationSeconds.toFloat(),
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    // Time indicators
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = formatDuration(currentPosition),
                                            fontSize = 12.sp,
                                            color = Color(0xFF757575)
                                        )

                                        Text(
                                            text = formatDuration(project.audioProject.durationSeconds),
                                            fontSize = 12.sp,
                                            color = Color(0xFF757575)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Text Content
                        Text(
                            text = "Nội dung văn bản",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF212121)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = project.audioProject.textContent,
                                    fontSize = 15.sp,
                                    color = Color(0xFF424242),
                                    lineHeight = 24.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Project statistics or additional info
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Thông tin thêm",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF212121)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                StatItem("Tổng số slides", project.slideNum.toString())
                                StatItem("Trạng thái", project.status.name.replace("_", " "))
                                StatItem("Thời lượng", formatDuration(project.audioProject.durationSeconds))
                                StatItem("Loại giọng đọc", project.audioProject.voiceType)
                                StatItem("Ngày chỉnh sửa cuối", formatDate(project.creationTime))
                                StatItem("ID dự án", project.id)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun formatDuration(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return "%d:%02d".format(minutes, remainingSeconds)
}

//@RequiresApi(Build.VERSION_CODES.O)
//private fun formatDate(timestamp: Long): String {
//    val instant = Instant.ofEpochMilli(timestamp)
//    val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
//    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//    return localDateTime.format(formatter)
//}

@Composable
fun SlidePreviewItem(slideNumber: Int, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Slide number indicator
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFE3F2FD), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = slideNumber.toString(),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF212121)
            )

            Text(
                text = "Slide content preview goes here...",
                fontSize = 14.sp,
                color = Color(0xFF757575)
            )
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF757575)
        )

        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF212121)
        )
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
fun ProjectDetailScreenPreview() {
    MaterialTheme {
        ProjectDetailScreen()
    }
}