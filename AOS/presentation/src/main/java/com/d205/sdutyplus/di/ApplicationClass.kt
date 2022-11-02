package com.d205.sdutyplus.di

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationClass: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "20a4b8e9f2dba673c27d4f83f44ac405")
    }
}