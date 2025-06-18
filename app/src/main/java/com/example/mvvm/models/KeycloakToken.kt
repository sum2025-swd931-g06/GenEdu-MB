package com.example.mvvm.models

data class TokenInfo(
    val exp: Long,
    val iat: Long,
    val jti: String,
    val iss: String,
    val aud: String,
    val sub: String,
    val typ: String,
    val azp: String,
    val sid: String,
    val acr: String,
    val allowedOrigins: List<String>,
    val realmAccess: RealmAccess,
    val resourceAccess: ResourceAccess,
    val scope: String,
    val emailVerified: Boolean,
    val name: String,
    val preferredUsername: String,
    val givenName: String,
    val familyName: String,
    val email: String,
    val clientId: String,
    val username: String,
    val tokenType: String,
    val active: Boolean
)

data class RealmAccess(
    val roles: List<String>
)

data class ResourceAccess(
    val account: AccountRoles
)

data class AccountRoles(
    val roles: List<String>
)
