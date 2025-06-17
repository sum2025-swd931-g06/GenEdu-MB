package com.example.mvvm.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mvvm.R
import com.example.mvvm.Screen
import com.example.mvvm.mock.sampleProjects
import com.example.mvvm.models.Project
import com.example.mvvm.ui.components.cards.ProjectCard
import com.example.mvvm.ui.components.featureitems.FeatureItem
import com.example.mvvm.utils.navigateTo

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
    username: String = "Fukada 🐢",
    projects: List<Project> = sampleProjects
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier.clickable { /* Handle menu click */ }
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier.clickable { /* Handle search click */ }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Hi $username", fontSize = 16.sp)
        Text(
            text = "Manage your project",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Upgrade card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFCBC4FF))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.earth_100),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Unlimited Storage", fontSize = 14.sp)
                    Text(
                        text = "$30/year",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Offer till May 26", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Button(
                        onClick = { /* TODO */ },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                    ) {
                        Text("Upgrade")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Features row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FeatureItem(
                iconRes = R.drawable.presentation_100,
                label = "Project",
                navigateTo = { navigateTo(navController, Screen.Project.route) }
            )
            FeatureItem(
                iconRes = R.drawable.audio_100,
                label = "Audio",
                navigateTo = { navigateTo(navController, Screen.Project.route) }
            )
            FeatureItem(
                iconRes = R.drawable.profile_100,
                label = "Profile",
                navigateTo = { navigateTo(navController, Screen.UserProfile.route) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recents
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Recents Project", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("View all", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.clickable {
                navigateTo(navController, Screen.Project.route)
            })
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Project list
        projects.forEach { project ->
            ProjectCard(navController, project)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()

    MaterialTheme {
        HomeScreen(
            navController,
            viewModel = HomeViewModel(null, null), // Create a simple mock ViewModel
            username = "Preview User",
            projects = sampleProjects
        )
    }

}