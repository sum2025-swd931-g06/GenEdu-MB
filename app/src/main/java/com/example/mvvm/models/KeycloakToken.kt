package com.example.mvvm.models

import com.google.gson.annotations.SerializedName

data class TokenIntrospectionResponse(
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
    @SerializedName("allowed-origins") val allowedOrigins: List<String>,
    @SerializedName("realm_access") val realmAccess: RealmAccess,
    @SerializedName("resource_access") val resourceAccess: ResourceAccess,
    val scope: String,
    @SerializedName("email_verified") val emailVerified: Boolean,
    val name: String,
    @SerializedName("preferred_username") val preferredUsername: String,
    @SerializedName("given_name") val givenName: String,
    @SerializedName("family_name") val familyName: String,
    val email: String,
    @SerializedName("client_id") val clientId: String,
    val username: String,
    @SerializedName("token_type") val tokenType: String,
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
