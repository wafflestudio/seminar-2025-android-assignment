package com.example.seminar_assignment_2025.network

// AuthInterceptor.kt (새 파일 생성)

import com.example.seminar_assignment_2025.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // 1. 기존 요청을 가져옵니다.
        val originalRequest = chain.request()

        // 2. 기존 URL에 'api_key' 쿼리 파라미터를 추가합니다.
        val newUrl = originalRequest.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .build()

        // 3. 새 URL로 요청을 다시 빌드합니다.
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        // 4. 새로운 요청으로 통신을 이어갑니다.
        return chain.proceed(newRequest)
    }
}