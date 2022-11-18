package com.d108.sduty.utils.sharedpreference

import com.d108.sduty.common.ApplicationClass

class FCMPreference {
    fun setFcmToken(token: String){
        ApplicationClass.fcmTokenPrefs.edit().putString("Token", token).apply()
    }

    fun getFcmToken(): String{
        return ApplicationClass.fcmTokenPrefs.getString("Token","").toString()
    }
}