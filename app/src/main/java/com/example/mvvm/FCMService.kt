package com.example.mvvm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.mvvm.models.UserDeviceTokenRequest
import com.example.mvvm.repositories.apis.notification.NotificationRepository
import com.example.mvvm.utils.DeviceUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FCMService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationRepository: NotificationRepository
    
    @Inject 
    lateinit var deviceUtils: DeviceUtils
    
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCMService", "New token received: $token")
        
        // Get current user email from SharedPreferences or wherever you store it
        val userEmail = getCurrentUserEmail()
        if (userEmail != null) {
            sendTokenToBackend(userEmail, token)
        } else {
            Log.w("FCMService", "User email not available, token will be sent after login")
        }
    }
    
    private fun getCurrentUserEmail(): String? {
        // Get user email from SharedPreferences where it was stored during login
        val sharedPrefs = getSharedPreferences("auth_prefs", MODE_PRIVATE)
        return sharedPrefs.getString("user_email", null)
    }
    
    private fun sendTokenToBackend(userEmail: String, fcmToken: String) {
        coroutineScope.launch {
            try {
                val request = UserDeviceTokenRequest(
                    email = userEmail, // Using email as the identifier
                    deviceId = deviceUtils.getDeviceId(),
                    fcmToken = fcmToken,
                    deviceName = deviceUtils.getDeviceName(),
                    platform = deviceUtils.getPlatform()
                )
                
                val result = notificationRepository.registerFcmToken(request)
                result.fold(
                    onSuccess = { response ->
                        Log.d("FCMService", "FCM token registered successfully: ${response.id}")
                    },
                    onFailure = { exception ->
                        Log.e("FCMService", "Failed to register FCM token", exception)
                    }
                )
            } catch (e: Exception) {
                Log.e("FCMService", "Error sending FCM token to backend", e)
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = 1
        val requestCode = 1

        val channelId = "Firebase Messaging ID"
        val channelName = "Firebase Messaging"
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            )
        }

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntentFlag = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) 0 else PendingIntent.FLAG_IMMUTABLE
        val pendingIntent = PendingIntent.getActivity(this, requestCode, intent, pendingIntentFlag)

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setSmallIcon(R.drawable.profile_100)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}