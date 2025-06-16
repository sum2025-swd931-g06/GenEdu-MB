package com.example.mvvm

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mvvm.ui.navigation.BottomNavHost
import com.example.mvvm.ui.screen.home.HomeScreen
import com.example.mvvm.ui.screen.home.HomeViewModel
import com.example.mvvm.ui.screen.intro.IntroScreen

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Home : Screen("home")
    object Detail : Screen("detail")
    object AddOrEdit : Screen("addOrEdit")
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

    // For screens that should include the bottom nav
    if (mainState.value.isAuthenticated) {
        BottomNavHost(navController = navController) { padding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Intro.route,
//                modifier = Modifier.padding(padding = padding)
            ) {
                composable(Screen.Home.route) {
                    val homeViewModel: HomeViewModel = hiltViewModel()
                    HomeScreen(
                        navController,
                        homeViewModel
                    )
                }
                // Add other screens
            }
        }
    } else {
        // For login/auth screens without bottom nav
        NavHost(navController = navController, startDestination = Screen.Intro.route) {
            composable(Screen.Intro.route) {
                IntroScreen(
                    navController = navController,
                    viewModel = hiltViewModel(),
                    mainViewModel = mainViewModel,
                    authResultLauncher = authResultLauncher,
                    setAuthResultCallback = setAuthResultCallback
                )
            }


        }
    }
}