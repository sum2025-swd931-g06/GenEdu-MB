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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.mvvm.ui.screen.account.GenEduLogo
import com.example.mvvm.ui.screen.project.Project
import com.example.mvvm.ui.screen.project.ProjectStatus
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
        slideNum = 24
    ),
    onBackClick: () -> Unit = {},
    onEditClick: (Project) -> Unit = {},
    onShareClick: (Project) -> Unit = {}
) {
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

            // GenEdu logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 6.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        GenEduLogo(modifier = Modifier.size(40.dp))
                    }
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
                                StatItem("Ngày chỉnh sửa cuối", formatDate(project.creationTime))
                                StatItem("ID dự án", project.id)
                            }
                        }
                    }

                    // Edit FAB
                    FloatingActionButton(
                        onClick = { onEditClick(project) },
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.BottomEnd),
                        containerColor = Color(0xFF2196F3),
                        contentColor = Color.White
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Project"
                        )
                    }
                }
            }
        }
    }
}

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