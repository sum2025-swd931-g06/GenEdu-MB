package com.example.mvvm.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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