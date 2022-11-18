package com.d205.data.common

import android.content.SharedPreferences
import android.util.Log
import com.d205.data.dao.UserSharedPreference
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


private const val TAG = "XAccessTokenInterceptor"
class XAccessTokenInterceptor @Inject constructor(
    private val sharedPref: UserSharedPreference
): Interceptor {
    private val noJwtUrls = arrayListOf("/api/user/kakao/login", "/api/user/naver/login")

    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url().encodedPath()
        Log.d(TAG, "intercept url: $url")
        if(noJwtUrls.contains(url)) {
            Log.d(TAG, "intercept: No JWT")
            return chain.proceed(chain.request().newBuilder()
                .build())
        }

        val token = sharedPref.getStringFromPreference("jwt")
        Log.d(TAG, "intercept token: $token")

        val request = chain.request().newBuilder()
            .addHeader("JWT-AUTHENTICATION", token)
            .build()

        Log.d(TAG, "intercept headers: ${request.headers()} ")
        Log.d(TAG, "intercept body : ${request.body()} ")

        return chain.proceed(request)
    }
}