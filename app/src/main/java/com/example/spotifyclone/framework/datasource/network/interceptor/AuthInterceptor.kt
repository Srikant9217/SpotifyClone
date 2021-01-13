package com.example.spotifyclone.framework.datasource.network.interceptor

import com.example.spotifyclone.framework.datasource.preferances.MyPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor
@Inject
constructor(
    private val preferences: MyPreferences
): Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain
            .request()
            .newBuilder()

        preferences.getToken()?.let { token->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}