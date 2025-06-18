package com.example.mvvm.repositories.apis.keycloak

import com.example.mvvm.models.TokenIntrospectionResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface KeycloakApi {
    @GET("realms/GenEdu/protocol/openid-connect/userinfo")
    suspend fun getUserInfo(
        @Header("Authorization") authorization: String
    ): Response<UserInfoResponse>

    @FormUrlEncoded
    @POST("realms/GenEdu/protocol/openid-connect/token/introspect")
    suspend fun introspectToken(
        @Field("token") token: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Response<TokenIntrospectionResponse>

    @FormUrlEncoded
    @POST("realms/GenEdu/protocol/openid-connect/logout")
    suspend fun logout(
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Response<Unit>
}

data class UserInfoResponse(
    val sub: String,
    val name: String?,
    val given_name: String?,
    val family_name: String?,
    val preferred_username: String?,
    val email: String?,
    val email_verified: Boolean?,
    val phone_number: String?,
    val phone_number_verified: Boolean?,
    val picture: String?,
    val locale: String?,
    val updated_at: Long?,
    // Add any custom attributes your Keycloak setup returns
    val custom_attributes: Map<String, Any>? = null
)