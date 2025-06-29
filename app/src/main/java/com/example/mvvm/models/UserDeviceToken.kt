package com.example.mvvm.models

import com.google.gson.annotations.SerializedName

data class UserDeviceTokenRequest(
    @SerializedName("email") val email: String,
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("fcmToken") val fcmToken: String,
    @SerializedName("deviceName") val deviceName: String,
    @SerializedName("platform") val platform: String
)

data class UserDeviceTokenResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("email") val email: String,
    @SerializedName("deviceId") val deviceId: String,
    @SerializedName("fcmToken") val fcmToken: String,
    @SerializedName("deviceName") val deviceName: String,
    @SerializedName("platform") val platform: String
)
