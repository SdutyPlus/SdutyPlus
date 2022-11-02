package com.d108.sduty.utils

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.d108.sduty.common.ApplicationClass.Companion.appLockPref
import com.d108.sduty.common.ApplicationClass.Companion.timerPref

private const val TAG = "AppLockAccessibilityService"
class AppLockAccessibilityService : AccessibilityService() {
    @SuppressLint("LongLogTag")
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 디바이스 화면이 변화할 때마다 이벤트 감지
        if(event!!.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            val isBanned = appLockPref.getBoolean(event.packageName.toString(), false)
            val isStartedTimer = !timerPref.getString("StartTime", "").isNullOrEmpty()

            // 밴 목록에 있거나, 타이머가 동작 중이라면 앱 종료
            if(isBanned && isStartedTimer){
                closeApp()
            }

            Log.e(TAG, "Catch Event Package Name : " + event.getPackageName());
            Log.e(TAG, "Catch Event TEXT : " + event.getText());
            Log.e(TAG, "Catch Event ContentDescription : " + event.getContentDescription());
            Log.e(TAG, "Catch Event getSource : " + event.getSource());
            Log.e(TAG, "=========================================================================");
        }

    }

    override fun onInterrupt() {
    }

    @SuppressLint("LongLogTag")
    private fun closeApp(){
        Intent().apply {
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS or Intent.FLAG_ACTIVITY_FORWARD_RESULT or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            startActivity(this)
        }
        Toast.makeText(applicationContext, "Sduty에서 차단한 앱입니다!\n타이머 측정을 종료하면 해제됩니다.", Toast.LENGTH_SHORT).show()
    }
}