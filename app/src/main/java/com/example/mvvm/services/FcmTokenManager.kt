package com.example.mvvm.services

import android.util.Log
import com.example.mvvm.models.UserDeviceTokenRequest
import com.example.mvvm.repositories.apis.notification.NotificationRepository
import com.example.mvvm.utils.DeviceUtils
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmTokenManager @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val deviceUtils: DeviceUtils
) {
    
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    
    fun registerTokenWithUserIdAndDevice(userEmail: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCMTokenManager", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("FCMTokenManager", "FCM Token: $token")
            
            // Send token to backend with device info using email
            sendTokenToBackend(userEmail, token)
        }
    }
    
    private fun sendTokenToBackend(userId: String, fcmToken: String) {
        coroutineScope.launch {
            try {
                val request = UserDeviceTokenRequest(
                    email = userId,
                    deviceId = deviceUtils.getDeviceId(),
                    fcmToken = fcmToken,
                    deviceName = deviceUtils.getDeviceName(),
                    platform = deviceUtils.getPlatform()
                )
                
                val result = notificationRepository.registerFcmToken(request)
                result.fold(
                    onSuccess = { response ->
                        Log.d("FCMTokenManager", "FCM token registered successfully: ${response.id}")
                    },
                    onFailure = { exception ->
                        Log.e("FCMTokenManager", "Failed to register FCM token", exception)
                    }
                )
            } catch (e: Exception) {
                Log.e("FCMTokenManager", "Error sending FCM token to backend", e)
            }
        }
    }
}
