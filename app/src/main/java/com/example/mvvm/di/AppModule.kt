package com.example.mvvm.di

import android.content.Context
import android.content.SharedPreferences
import com.example.mvvm.data.local.SharedPreferencesTokenProvider
import com.example.mvvm.repositories.FinalizedLectureRepository
import com.example.mvvm.repositories.apis.project.FinalizedLectureApi
import com.example.mvvm.security.TokenProvider
import com.example.mvvm.utils.DeviceUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideDeviceUtils(@ApplicationContext context: Context): DeviceUtils {
        return DeviceUtils(context)
    }

    @Provides
    @Singleton
    fun provideFinalizedLectureApi(retrofit: Retrofit): FinalizedLectureApi {
        return retrofit.create(FinalizedLectureApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFinalizedLectureRepository(
        api: FinalizedLectureApi,
        tokenProvider: SharedPreferencesTokenProvider
    ): FinalizedLectureRepository {
        return FinalizedLectureRepository(api, tokenProvider)
    }
}