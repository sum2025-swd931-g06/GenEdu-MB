package com.example.mvvm.repositories.apis.keycloak

import com.example.mvvm.models.TokenIntrospectionResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface KeycloakApi {
    @FormUrlEncoded
    @POST("realms/shoppe/protocol/openid-connect/token/introspect")
    suspend fun introspectToken(
        @Field("token") token: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Response<TokenIntrospectionResponse>

    @FormUrlEncoded
    @POST("realms/shoppe/protocol/openid-connect/logout")
    suspend fun logout(
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String
    ): Response<Unit>
}