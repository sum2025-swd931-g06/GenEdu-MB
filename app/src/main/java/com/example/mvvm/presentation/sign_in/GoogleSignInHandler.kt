package com.example.mvvm.presentation.sign_in

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun GoogleSignInHandler(
    googleAuthUiClient: GoogleAuthUiClient,
    lifecycleScope: CoroutineScope,
    onSignInResult: (SignInResult) -> Unit,
    triggerSignIn: Boolean,
    onSignInLaunched: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    onSignInResult(signInResult)
                }
            }
        }
    )

    LaunchedEffect(triggerSignIn) {
        if (triggerSignIn) {
            lifecycleScope.launch {
                val signInIntentSender = googleAuthUiClient.signIn()
                signInIntentSender?.let {
                    launcher.launch(IntentSenderRequest.Builder(it).build())
                    onSignInLaunched()
                }
            }
        }
    }
}
