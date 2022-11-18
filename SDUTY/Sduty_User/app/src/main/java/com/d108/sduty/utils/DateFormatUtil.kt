package com.d108.sduty.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

private const val TAG ="DateFormatUtil"
class DateFormatUtil {
    companion object{
        fun converYYYYMMDD(date: String): Date? {
            try {
                val formatter = SimpleDateFormat("yyyyMMdd", Locale("ko","KR"))
                formatter.timeZone = TimeZone.getTimeZone("Asia/Seoul")
                return formatter.parse(date)
            } catch (e: Exception) {
                Log.d(TAG, "converYYYYMMDD: ${e.message}")
            }
            return null
        }

    }
}