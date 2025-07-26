package com.example.mvvm.repositories.apis

import com.example.mvvm.repositories.apis.project.FinalizedLectureApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://genedu-gateway.lch.id.vn/api/v1/"
    private var authToken: String? = null

    fun setAuthToken(token: String) {
        authToken = token
    }

    private val client: OkHttpClient
        get() = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                authToken?.let {
                    requestBuilder.addHeader("Authorization", "Bearer $it")
                }
                chain.proceed(requestBuilder.build())
            }
            .build()

    private val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val finalizedLectureApi: FinalizedLectureApi
        get() = retrofit.create(FinalizedLectureApi::class.java)
}

