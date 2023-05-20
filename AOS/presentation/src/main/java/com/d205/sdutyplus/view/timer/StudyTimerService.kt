package com.d205.sdutyplus.view.timer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.d205.sdutyplus.R
import java.util.*

class StudyTimerService : Service() {

    companion object {
        //channel ID for Notification
        const val CHANNEL_ID = "Timer_Notification"

        //service extra
        const val STUDY_TIMER_ACTION = "STUDY_TIMER_ACTION"

        //service actions
        const val SERVICE_ACTION = "SERVICE_ACTION"
        const val START_TIMER = "START"
        const val STOP_TIMER = "STOP"
        const val RESET_TIMER = "RESET"
        const val MOVE_TO_FOREGROUND = "MOVE_TO_FOREGROUND"
        const val MOVE_TO_BACKGROUND = "MOVE_TO_BACKGROUND"

        //current timer's elapsedTime
        private val _timertime =  MutableLiveData<Int>(0)
        val serviceTimerTime: LiveData<Int>
            get() = _timertime

        private val _isTimerRunning = MutableLiveData<Boolean>(false)
        val isServiceTimerRunning: LiveData<Boolean>
            get() = _isTimerRunning

    }

    private lateinit var studyTimer: Timer
    private lateinit var notificationManager: NotificationManager
    private var isForeground: Boolean = false


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        createChannel()
        // foreground 시 notification update
        getNotificationManager()

        // Activity로부터 Service가 해야하는 동작 가져오기
        val action = intent?.getStringExtra(STUDY_TIMER_ACTION)!!
        Log.d("StudyTimer", "onStartCommand StudyTimer Action: $action")

        // action에 따라 수행할 로직 구현하기
        when (action) {
            START_TIMER -> startTimer()
            STOP_TIMER -> stopTimer()
            RESET_TIMER -> resetTimer()
            MOVE_TO_FOREGROUND -> moveToForeground()
            MOVE_TO_BACKGROUND -> moveToBackground()
        }

        return START_STICKY
    }

    private fun startTimer() {
        _isTimerRunning.postValue(true)

        studyTimer = Timer()
        studyTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() { // 1초마다 타이머 동작
                _timertime.postValue(_timertime.value!! + 1)

                if(isForeground) {
                    updateNotification()
                }
            }
        }, 0, 1000)

    }

    private fun stopTimer() {
        studyTimer.cancel()
        _isTimerRunning.postValue(false)
    }

    private fun resetTimer() {
        _timertime.postValue(0)
    }

    private fun moveToForeground() {
        if (_isTimerRunning.value!!) {
            Log.d("StudyTimer", "StudyTimer Foreground Start")

            isForeground = true
            startForeground(1, buildNotification())
        }
    }

    private fun moveToBackground() {

    }

    private fun createChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID, "Study Timer", NotificationManager.IMPORTANCE_DEFAULT).apply {
                setSound(null, null)
                setShowBadge(true)
            }
            val notificationManager = getSystemService(NotificationManager::class.java).apply {
                createNotificationChannel(notificationChannel)
            }
        }
    }

    private fun buildNotification(): Notification {
        val builder : NotificationCompat.Builder
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = NotificationCompat.Builder(this, CHANNEL_ID)
        } else {
            builder = NotificationCompat.Builder(this)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }
        builder.apply {
            setSmallIcon(R.drawable.ic_clock)
            setContentTitle("Sduty+ Timer")
            setContentText("${_timertime.value}")
        }
        return builder.build()
    }

    private fun getNotificationManager() {
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
    }

    private fun updateNotification() {
        notificationManager.notify(
            1,
            buildNotification()
        )
    }

}