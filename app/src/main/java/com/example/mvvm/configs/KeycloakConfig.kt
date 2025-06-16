package com.example.mvvm.configs

object KeycloakAuthConfig {
    const val REALM_NAME = "GenEdu"
    const val AUTH_ENDPOINT = "https://kc.lch.id.vn/realms/${REALM_NAME}/protocol/openid-connect/auth"
    const val TOKEN_ENDPOINT = "https://kc.lch.id.vn/realms/${REALM_NAME}/protocol/openid-connect/token"
    const val LOGOUT_ENDPOINT = "https://kc.lch.id.vn/realms/${REALM_NAME}/protocol/openid-connect/logout"
    const val CLIENT_ID = "genedu-mb"
    const val REDIRECT_URI = "com.example.mvvm://callback"
    const val SCOPE = "openid profile email"
}
