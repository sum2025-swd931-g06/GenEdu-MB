package com.example.mvvm.di

import android.content.Context
import com.example.mvvm.repositories.SharedPreferencesTokenProvider
import com.example.mvvm.repositories.apis.keycloak.KeycloakApi
import com.example.mvvm.repositories.apis.keycloak.KeycloakRepository
import com.example.mvvm.repositories.apis.project.ProjectApi
import com.example.mvvm.repositories.apis.project.ProjectRepository
import com.example.mvvm.security.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val BASE_URL = "http://10.0.2.2:4003/"
    private val MOCKY_BASE_URL = "https://run.mocky.io/"

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenProvider: TokenProvider): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val token = tokenProvider.getToken()

            val requestBuilder: Request.Builder = original.newBuilder()
            token?.let {
                requestBuilder.header("Authorization", "Bearer $it")
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideTokenProvider(@ApplicationContext context: Context): TokenProvider {
        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return SharedPreferencesTokenProvider(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideCookieManager(): CookieManager {
        return CookieManager().apply {
            setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: Interceptor, cookieManager: CookieManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .cookieJar(JavaNetCookieJar(cookieManager))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Keycloak 111
    @Provides
    @Singleton
    fun provideKeycloakApi(okHttpClient: OkHttpClient): KeycloakApi {
        return Retrofit.Builder()
            .baseUrl("https://kc.lch.id.vn/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KeycloakApi::class.java)
    }

    @Provides
    @Singleton
    fun provideKeycloakRepository(
        api: KeycloakApi,
        tokenProvider: SharedPreferencesTokenProvider
    ): KeycloakRepository {
        return KeycloakRepository(api, tokenProvider)
    }

    @Provides
    @Singleton
    fun provideProjectApi(okHttpClient: OkHttpClient): ProjectApi {
        return Retrofit.Builder()
            .baseUrl("https://685656fd1789e182b37db664.mockapi.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProjectApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProjectRepository(
        api: ProjectApi,
        tokenProvider: SharedPreferencesTokenProvider
    ): ProjectRepository {
        return ProjectRepository(api, tokenProvider)
    }

}