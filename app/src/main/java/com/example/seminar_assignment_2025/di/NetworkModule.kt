package com.example.seminar_assignment_2025.di // ⬅️ 새 di 패키지

import com.example.seminar_assignment_2025.BuildConfig
import com.example.seminar_assignment_2025.network.AuthInterceptor
import com.example.seminar_assignment_2025.network.TmdbApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module // ⬅️ "이것은 Hilt 모듈입니다"
@InstallIn(SingletonComponent::class) // ⬅️ "이 모듈의 객체들은 앱 전역에서 사용됩니다 (Singleton)"
object NetworkModule {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    // 1. HttpLoggingInterceptor 제공
    @Provides
    @Singleton // ⬅️ 앱 내에서 단 하나의 인스턴스만 생성
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    // 2. AuthInterceptor 제공
    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor() // AuthInterceptor도 Hilt가 만들도록 함
    }

    // 3. OkHttpClient 제공 (Hilt가 1, 2번을 자동으로 주입해줌)
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor, // ⬅️ Hilt가 1번에서 만든 것을 줌
        authInterceptor: AuthInterceptor           // ⬅️ Hilt가 2번에서 만든 것을 줌
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // 4. Kotlinx-Serialization Json 객체 제공
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    // 5. Retrofit 제공 (Hilt가 3, 4번을 자동으로 주입해줌)
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient, // ⬅️ Hilt가 3번에서 만든 것을 줌
        json: Json                  // ⬅️ Hilt가 4번에서 만든 것을 줌
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    // 6. TmdbApiService 제공 (Hilt가 5번을 자동으로 주입해줌)
    @Provides
    @Singleton
    fun provideTmdbApiService(retrofit: Retrofit): TmdbApiService { // ⬅️ Hilt가 5번에서 만든 것을 줌
        return retrofit.create(TmdbApiService::class.java)
    }
}