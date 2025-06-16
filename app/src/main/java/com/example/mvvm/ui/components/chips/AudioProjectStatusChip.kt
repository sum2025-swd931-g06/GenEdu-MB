package com.example.mvvm.ui.components.chips

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mvvm.models.AudioProjectStatus

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