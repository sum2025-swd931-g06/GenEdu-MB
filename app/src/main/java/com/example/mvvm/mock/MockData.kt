package com.example.mvvm.mock

import com.example.mvvm.models.AudioProject
import com.example.mvvm.models.AudioProjectStatus
import com.example.mvvm.models.Project
import com.example.mvvm.models.ProjectStatus

val sampleProjects = listOf(
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
            textContent = "Việt Nam là một quốc gia...",
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
        title = "Toán học nâng cao - Đại số",
        status = ProjectStatus.DRAFT,
        creationTime = System.currentTimeMillis() - 86400000 * 3,
        slideNum = 30,
        audioProject = null
    ),
    Project(
        id = "4",
        title = "Văn học dân gian",
        status = ProjectStatus.IN_PROGRESS,
        creationTime = System.currentTimeMillis() - 86400000 * 4,
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
    Project(
        id = "5",
        title = "Lịch sử thế giới hiện đại",
        status = ProjectStatus.COMPLETED,
        creationTime = System.currentTimeMillis() - 86400000 * 5,
        slideNum = 18,
        audioProject = null
    ),
    Project(
        id = "6",
        title = "Sinh học phân tử",
        status = ProjectStatus.IN_PROGRESS,
        creationTime = System.currentTimeMillis() - 86400000 * 6,
        slideNum = 20,
        audioProject = AudioProject(
            id = "6",
            title = "DNA và ARN",
            status = AudioProjectStatus.COMPLETED,
            creationTime = System.currentTimeMillis() - 86400000 * 6,
            durationSeconds = 198,
            textContent = "Cấu trúc và chức năng của DNA, ARN...",
            audioUrl = "https://example.com/audio/678901.mp3",
            voiceType = "Nam miền Bắc"
        )
    ),
    Project(
        id = "7",
        title = "Tin học căn bản",
        status = ProjectStatus.DRAFT,
        creationTime = System.currentTimeMillis() - 86400000 * 7,
        slideNum = 10,
        audioProject = null
    ),
    Project(
        id = "8",
        title = "Tiếng Anh giao tiếp",
        status = ProjectStatus.IN_PROGRESS,
        creationTime = System.currentTimeMillis() - 86400000 * 1,
        slideNum = 25,
        audioProject = AudioProject(
            id = "8",
            title = "Everyday English",
            status = AudioProjectStatus.PROCESSING,
            creationTime = System.currentTimeMillis() - 86400000,
            durationSeconds = 250,
            textContent = "Hello! How are you today?...",
            audioUrl = "https://example.com/audio/888888.mp3",
            voiceType = "Nữ giọng Mỹ"
        )
    ),
    Project(
        id = "9",
        title = "Vật lý điện học",
        status = ProjectStatus.COMPLETED,
        creationTime = System.currentTimeMillis() - 86400000 * 8,
        slideNum = 14,
        audioProject = AudioProject(
            id = "9",
            title = "Điện trở và dòng điện",
            status = AudioProjectStatus.COMPLETED,
            creationTime = System.currentTimeMillis() - 86400000 * 8,
            durationSeconds = 165,
            textContent = "Điện trở, định luật Ohm và mạch điện cơ bản.",
            audioUrl = "https://example.com/audio/999999.mp3",
            voiceType = "Nam miền Trung"
        )
    ),
    Project(
        id = "10",
        title = "Địa lý kinh tế",
        status = ProjectStatus.DRAFT,
        creationTime = System.currentTimeMillis() - 86400000 * 10,
        slideNum = 16,
        audioProject = null
    )
)
