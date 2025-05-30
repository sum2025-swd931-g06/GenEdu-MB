package com.example.mvvm.ui.screen.login

import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mvvm.MainViewModel
import com.example.mvvm.R
import com.example.mvvm.Screen
import com.example.mvvm.ui.theme.CustomTextField
import org.com.hcmurs.common.enum.LoadStatus

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
    val greetingText = "Login to GenEdu System"
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = greetingText,
//            style = MaterialTheme.typography.headlineMedium
            fontSize = MaterialTheme.typography.headlineMedium.fontSize
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = username,
            onValueChange = { username = it },
            labelText = "Username",
            errorText = usernameError,
            leadingIcon = Icons.Rounded.AccountCircle
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomTextField(
            value = password,
            onValueChange = { password = it },
            labelText = "Password",
            errorText = passwordError,
            leadingIcon = Icons.Rounded.Lock,
            isPassword = true,
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = { passwordVisible = !passwordVisible }
        )


        Spacer(modifier = Modifier.height(16.dp))

        when (status) {
            is LoadStatus.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Authenticating...")
            }
            else -> {
                Button(
                    onClick = {
                        usernameError = if (username.isEmpty()) "Username is required" else ""
                        passwordError = if (password.isEmpty()) "Password is required" else ""
                        if (usernameError.isEmpty() && passwordError.isEmpty()) {
                            // Perform login logic here
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp)
                ) {
                    Text("Login with Keycloak")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Forget password?",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                // Handle forget password click
            }
        )

        Spacer(modifier = Modifier.height(50.dp))

        Row {
            Text(text = "Don't have an account? ")
            Text(
                text = "Sign in",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    // Handle register click
                }
            )
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