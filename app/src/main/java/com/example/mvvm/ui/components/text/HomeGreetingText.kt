package com.example.mvvm.ui.components.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun HomeGreetingText(
    name: String,
){
    Text(
        text = "Hi $name 👋",
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

}