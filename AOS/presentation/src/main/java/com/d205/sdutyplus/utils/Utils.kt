package com.d205.sdutyplus.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

fun getTodayDate(): Date {
    return Date(System.currentTimeMillis())
}

fun convertTimeDateToString(date: Date, format: String = "yyyy년 M월 d일"): String {
    val simpleDateFormat = SimpleDateFormat(format, Locale("ko", "KR"))
    return simpleDateFormat.format(date)
}

fun convertTimeStringToDate(str: String, format: String): Date {
    val simpleDateFormat = SimpleDateFormat(format, Locale("ko", "KR"))
    return simpleDateFormat.parse(str)
}

fun getTodayDateString(): String {
    val simpleDateFormat = SimpleDateFormat("yy.MM.dd", Locale("ko", "KR"))
    val currentDate = simpleDateFormat.format(Date())

    return currentDate
}

fun getDeviceSize(activity: Activity): Point {
    val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.currentWindowMetricsPointCompat()
}

