package com.d205.data.dao

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.d205.data.common.NOT_FOUND_STRING
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TimerSharedPreference @Inject constructor(
    @ApplicationContext override val applicationContext: Context
): SharedPreferenceHelper {

    override val PREFERENCES_NAME: String = "timerSharedPreference"
    override val sharedPreference: SharedPreferences = applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    override val sharedPreferenceEditor: SharedPreferences.Editor by lazy {
        sharedPreference.edit()
    }


    override fun setStringFromPreference(key: String, value: String): Boolean {
        sharedPreferenceEditor.apply {
            putString(key, value)
            commit()
        }
        return sharedPreference.getString(key, NOT_FOUND_STRING)!! == value
    }

    override fun getStringFromPreference(key: String): String {
        return sharedPreference.getString(key, NOT_FOUND_STRING)!!
    }

     fun setIntFromPreference(key: String, value: Int): Boolean {
        sharedPreferenceEditor.apply {
            putInt(key, value)
            commit()
        }
        return sharedPreference.getInt(key, 0)!! == value
    }

     fun getIntFromPreference(key: String): Int {
        return sharedPreference.getInt(key, 0)!!
    }

    override fun removeFromPreference(key: String): Boolean {
        sharedPreferenceEditor.apply {
            remove(key)
            commit()
        }
        return sharedPreference.getString(key, NOT_FOUND_STRING)!! == NOT_FOUND_STRING
    }

    override fun clearPreference(): Boolean {
        sharedPreferenceEditor.apply {
            clear()
            commit()
        }
        return sharedPreference.all.isEmpty()
    }

}