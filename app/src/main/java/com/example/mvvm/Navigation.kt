package com.example.mvvm

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mvvm.ui.navigation.BottomNavHost
import com.example.mvvm.ui.screen.account.AccountScreen
import com.example.mvvm.ui.screen.home.HomeScreen
import com.example.mvvm.ui.screen.home.HomeViewModel
import com.example.mvvm.ui.screen.intro.IntroScreen
import com.example.mvvm.ui.screen.project.ProjectScreen
import com.example.mvvm.ui.screen.projectdetail.ProjectDetailScreen

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Home : Screen("home")
    object Project: Screen("project")
    object ProjectDetail: Screen("projectDetail/{projectId}") {
        fun createRoute(projectId: String): String = "projectDetail/$projectId"
    }
    object UserProfile: Screen("userProfile")
}

//https://developer.android.com/topic/architecture
//https://developer.android.com/topic/libraries/architecture/viewmodel
//https://developer.android.com/training/dependency-injection
//https://developer.android.com/develop/ui/compose/libraries#hilt
//https://github.com/android/architecture-samples

@Composable
fun Navigation(
    authResultLauncher: ActivityResultLauncher<Intent>? = null,
    setAuthResultCallback: ((Intent?) -> Unit) -> Unit = {}
) {
    val navController = rememberNavController()
    val mainViewModel: MainViewModel = hiltViewModel()
    val mainState = mainViewModel.uiState.collectAsState()
    val context = LocalContext.current

        // For login/auth screens without bottom nav
        NavHost(navController = navController, startDestination = Screen.Home.route) {
            composable(Screen.Intro.route) {
                IntroScreen(
                    navController = navController,
                    viewModel = hiltViewModel(),
                    mainViewModel = mainViewModel,
                    authResultLauncher = authResultLauncher,
                    setAuthResultCallback = setAuthResultCallback
                )
            }

            composable(Screen.Home.route){
                HomeScreen(
                    navController = navController,
                    viewModel = hiltViewModel<HomeViewModel>(),
                    mainViewModel = mainViewModel
                )
            }

            composable(Screen.Project.route){
                ProjectScreen(
                    navController = navController
                )
            }

            composable(
                route = Screen.ProjectDetail.route,
                arguments = listOf(
                    navArgument("projectId") {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
                ProjectDetailScreen(
                    navController = navController,
                    projectId = projectId
                )
            }

            composable(Screen.UserProfile.route){
                AccountScreen(
                    navController = navController,
                    mainViewModel = mainViewModel,
                )
            }

        }
}