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
    override fun intercept(chain: Interceptor.Chain): Response {
//        val token = runBlocking {
//            sharedPref.getString("jwt","")!!
//        }
        val token = sharedPref.getStringFromPreference("jwt")
        Log.d(TAG, "intercept token: $token")
        val request = chain.request().newBuilder()
            .addHeader("JWT-AUTHENTICATION", token)
            .build()
        Log.d(TAG, "intercept headers: ${request.headers} ")
        Log.d(TAG, "intercept body : ${request.body} ")
        return chain.proceed(request)
    }
}