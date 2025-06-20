package com.example.mvvm.ui.screen.intro

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mvvm.MainViewModel
import com.example.mvvm.R
import com.example.mvvm.Screen
import com.example.mvvm.enum.LoadStatus
import com.example.mvvm.ui.theme.LoadingSpinnerColor
import com.example.mvvm.ui.theme.MainColor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IntroScreen(
    navController: NavHostController,
    viewModel: IntroViewModel,
    mainViewModel: MainViewModel,
    authResultLauncher: ActivityResultLauncher<Intent>? = null,
    setAuthResultCallback: ((Intent?) -> Unit) -> Unit = {}
) {
    val state = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Handle authentication success
    LaunchedEffect(state.value.isAuthenticated, state.value.userData) {
        if (state.value.isAuthenticated && state.value.userData != null) {
            // Log the state
            Log.d("IntroScreen", "Auth state updated - authenticated: ${state.value.isAuthenticated}, userData: ${state.value.userData}")

            // Update MainViewModel
            viewModel.updateMainViewModel(
                mainViewModel = mainViewModel,
                userData = state.value.userData!!,
                isAuthenticated = true
            )

            // Navigate to Home screen
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Intro.route) { inclusive = true }
            }
        }
    }

    // Handle auth result callback
    LaunchedEffect(Unit) {
        setAuthResultCallback { intent ->
            Log.d("IntroScreen", "Received auth result")
            viewModel.handleAuthResult(intent)
        }
    }

    IntroScreenContent(
        status = state.value.status,
        onLoginClick = {
            Log.d("IntroScreen", "Starting login flow")
            viewModel.startLogin(context, authResultLauncher)
        }
    )
}

@Composable
private fun IntroScreenContent(
    status: LoadStatus,
    onLoginClick: () -> Unit = {}
) {
    val greetingText = "Welcome to GenEdu System"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = painterResource(id = R.drawable.intro_logo),
            contentDescription = null,
            modifier = Modifier
                .size(300.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = greetingText,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Sign in to explore educational tools.\n" +
                    "Don’t have an account? Register now\n" +
                    "for a seamless and secure experience.",
            modifier = Modifier.padding(horizontal = 8.dp),
            color = Color(0xFF444444),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (status) {
            is LoadStatus.Loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = MainColor,
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Authenticating...",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )
                }
            }

            else -> {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LoadingSpinnerColor
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = "Let’s Get Started",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun IntroScreenPreview() {
    IntroScreenContent(
        status = LoadStatus.Init(),
        onLoginClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun IntroScreenLoadingPreview() {
    IntroScreenContent(
        status = LoadStatus.Loading(),
        onLoginClick = {}
    )
}