package com.d205.sdutyplus.uitls

import java.text.SimpleDateFormat
import java.util.*

fun getTodayDate(): Date {
    return Date(System.currentTimeMillis())
}

fun convertTimeDateToString(date: Date, format: String = "yyyy년 M월 d일"): String {
    val simpleDateFormat = SimpleDateFormat(format, Locale("ko", "KR"))
    return simpleDateFormat.format(date)
}