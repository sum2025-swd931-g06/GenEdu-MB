package com.example.mvvm.ui.screen.login

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mvvm.MainViewModel
import com.example.mvvm.Screen
import org.com.hcmurs.common.enum.LoadStatus
import com.example.mvvm.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel,
    mainViewModel: MainViewModel,
    authResultLauncher: ActivityResultLauncher<Intent>? = null,
    setAuthResultCallback: ((Intent?) -> Unit) -> Unit = {}
) {
    val state = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Set the callback for auth results
    LaunchedEffect(Unit) {
        setAuthResultCallback { intent ->
            viewModel.handleAuthResult(intent)
        }
    }

    // Handle authentication success
    LaunchedEffect(state.value.isAuthenticated) {
        if (state.value.isAuthenticated) {
            mainViewModel.setAuthenticated(true)
            navController.navigate(Screen.HomeMetro.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    // Handle errors
    LaunchedEffect(state.value.status) {
        if (state.value.status is LoadStatus.Error) {
            mainViewModel.setError(state.value.status.description)
            viewModel.resetStatus()
        }
    }

    LoginScreenContent(
        status = state.value.status,
        onLoginClick = {
            viewModel.startLogin(context, authResultLauncher)
        }
    )
}

@Composable
private fun LoginScreenContent(
    status: LoadStatus,
    onLoginClick: () -> Unit = {}
) {
    val greetingText = "Welcome to GenEdu System"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.intro_logo),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 24.dp),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = greetingText,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Sign in to explore a wide range of educational tools\n" +
                        "Don’t have an account yet?\n" +
                        " Register now and enjoy a seamless\n" +
                        "and secure shopping experience",
                modifier = Modifier.padding(vertical = 16.dp),
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            when (status) {
                is LoadStatus.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF6200EE),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Authenticating...",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray
                        )
                    }
                }

                else -> {

                    Button(
                        onClick = { Log.i("LOG", "hehe") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EE)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "Let's Get Started",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account?",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            modifier = Modifier.clickable{
                                onLoginClick()
                            },
                            text = "Sign in here",
                            fontSize = 16.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,

                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        status = LoadStatus.Init(),
        onLoginClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    LoginScreenContent(
        status = LoadStatus.Loading(),
        onLoginClick = {}
    )
}