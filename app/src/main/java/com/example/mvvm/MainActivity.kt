package com.example.mvvm

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.mvvm.services.FcmTokenManager
import com.example.mvvm.ui.theme.MVVMTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var authResultCallback: ((Intent?) -> Unit)? = null
    
    @Inject
    lateinit var fcmTokenManager: FcmTokenManager

    private val authResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Pass the result to the callback set by LoginViewModel
        authResultCallback?.invoke(result.data)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d("FCM", token.toString())
//            Toast.makeText(baseContext, token.toString(), Toast.LENGTH_SHORT).show()
            
            // Register token with backend when user is logged in
            // Note: You should call fcmTokenManager.registerTokenWithUserIdAndDevice(userEmail) 
            // after successful login in your authentication flow
        })

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVVMTheme {
                Navigation(
                    authResultLauncher = authResultLauncher,
                    setAuthResultCallback = { callback ->
                        authResultCallback = callback
                    }
                )
            }
        }
    }
}