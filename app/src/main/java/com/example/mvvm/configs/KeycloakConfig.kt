package com.example.mvvm.configs

import com.example.mvvm.utils.DeviceUtils

object KeycloakAuthConfig {
    const val isDevMode = false
    val IS_USER_REAL_DEVICE = !DeviceUtils.isEmulator
    const val KEYCLOAK_DEV_URL_FOR_EMULATOR = "http://10.0.2.2:9099/"
    const val KEYCLOAK_DEV_URL_FOR_REAL_DEVICE = "http://192.168.88.172:9099/"
    const val KEYCLOAK_PRODUCTION_URL = "https://kc.lch.id.vn/"
    const val REALM_NAME = "GenEdu"
    const val REDIRECT_URI = "com.example.mvvm://callback"
    const val SCOPE = "openid profile email"
    const val CLIENT_ID = "genedu-mb"

    // get keycloak url based on environment
    val KEYCLOAK_URL: String
        get() = when {
            !isDevMode -> KEYCLOAK_PRODUCTION_URL
            IS_USER_REAL_DEVICE -> KEYCLOAK_DEV_URL_FOR_REAL_DEVICE
            else -> KEYCLOAK_DEV_URL_FOR_EMULATOR
        }

    val AUTH_ENDPOINT: String
        get() = "${KEYCLOAK_URL}realms/$REALM_NAME/protocol/openid-connect/auth"

    val TOKEN_ENDPOINT: String
        get() = "${KEYCLOAK_URL}realms/$REALM_NAME/protocol/openid-connect/token"

    val LOGOUT_ENDPOINT: String
        get() = "${KEYCLOAK_URL}realms/$REALM_NAME/protocol/openid-connect/logout"

    val CLIENT_SECRET: String
        get() = if (isDevMode) "VOUvMpEx9ffkgV75Wt4QTfwseKwunrLK" else "WNbul1BdiuWrveuazHyPwYmGyJkQ6tlB"
}

