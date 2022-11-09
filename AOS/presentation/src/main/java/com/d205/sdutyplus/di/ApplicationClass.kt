package com.d205.sdutyplus.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass: Application() {
    companion object {
        lateinit var userPrefs: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "20a4b8e9f2dba673c27d4f83f44ac405")
        userPrefs = applicationContext.getSharedPreferences("userSharedPreference", Context.MODE_PRIVATE)
    }

    fun getJwt() = userPrefs.getString("jwt", "")
}