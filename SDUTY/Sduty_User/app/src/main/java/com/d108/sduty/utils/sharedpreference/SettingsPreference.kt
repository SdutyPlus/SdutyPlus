package com.d108.sduty.utils

import com.d108.sduty.common.ApplicationClass

class SettingsPreference {
    companion object

    fun setPushState(accept: Int){
        ApplicationClass.pushStateAll.edit().putInt("State", accept).apply()
    }
    fun getPushState(): Int{
        return ApplicationClass.pushStateAll.getInt("State", 2)
    }

    fun setAutoLoginState(accept: Boolean){
        ApplicationClass.autoLoginState.edit().putBoolean("State", accept).apply()
    }
    fun getAutoLoginState(): Boolean{
        return ApplicationClass.autoLoginState.getBoolean("State", true)
    }

    fun setUserId(userId: String){
        ApplicationClass.userPref.edit().putString("UserId", userId).apply()
    }

    fun getUserId(): String{
        return ApplicationClass.userPref.getString("UserId", "").toString()
    }
//    fun getDarkModeState(): Boolean{
//        return ApplicationClass.darkModeState.getBoolean("State", true)
//    }
//    fun setDarkModeState(accept: Boolean){
//        ApplicationClass.darkModeState.edit().putBoolean("State", accept).apply()
//    }

    fun getFirstRunCheck(): Boolean{
        return ApplicationClass.firstRunCheck.getBoolean("State", true)
    }

    fun setFirstRunCheck(accept: Boolean){
        ApplicationClass.firstRunCheck.edit().putBoolean("State", accept).apply()
    }

    fun getFirstLoginCheck(): Boolean{
        return ApplicationClass.firstLoginCheck.getBoolean("State", true)
    }

    fun setFirstLoginCheck(accept: Boolean){
        ApplicationClass.firstLoginCheck.edit().putBoolean("State", accept).apply()
    }
}