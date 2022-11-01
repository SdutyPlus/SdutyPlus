package com.d205.data.dao

import android.content.Context
import android.content.SharedPreferences

interface SharedPreferenceHelper {
     val applicationContext: Context
     val PREFERENCES_NAME: String
     val sharedPreference: SharedPreferences
     val sharedPreferenceEditor: SharedPreferences.Editor

     fun setStringFromPreference(key: String, value: String): Boolean
     fun getStringFromPreference(Key: String): String

     fun removeFromPreference(key: String): Boolean

     fun clearPreference(): Boolean

}