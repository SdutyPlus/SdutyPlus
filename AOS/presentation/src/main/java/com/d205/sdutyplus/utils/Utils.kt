package com.d205.sdutyplus.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.widget.Toast

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun getDeviceSize(activity: Activity): Point {
    val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.currentWindowMetricsPointCompat()
}

