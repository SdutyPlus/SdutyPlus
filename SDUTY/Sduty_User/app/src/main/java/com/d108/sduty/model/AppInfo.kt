package com.d108.sduty.model

import android.graphics.drawable.Drawable

data class AppInfo(val icon: Drawable, val label: String, val packageName: String, var isBanned : Boolean) {
}