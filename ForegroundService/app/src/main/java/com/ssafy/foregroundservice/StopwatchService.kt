package com.ssafy.foregroundservice

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

class StopwatchService : Service() {


    companion object {
        // Channel ID for notifications
        const val CHANNEL_ID = "Stopwatch_Notifications"

        // Service Actions
        const val START = "START"
        const val PAUSE = "PAUSE"
        const val RESET = "RESET"
        const val GET_STATUS = "GET_STATUS"
        const val MOVE_TO_FOREGROUND = "MOVE_TO_FOREGROUND"
        const val MOVE_TO_BACKGROUND = "MOVE_TO_BACKGROUND"

        // Intent Extras
        const val STOPWATCH_ACTION = "STOPWATCH_ACTION"
        const val TIME_ELAPSED = "TIME_ELAPSED"
        const val IS_STOPWATCH_RUNNING = "IS_STOPWATCH_RUNNING"

        // Intent Actions
        const val STOPWATCH_TICK = "STOPWATCH_TICK"
        const val STOPWATCH_STATUS = "STOPWATCH_STATUS"

        private val _livetime =  MutableLiveData<Int>(0)
        val livetime: LiveData<Int>
            get() = _livetime
    }

    private lateinit var notificationManager: NotificationManager

    private var timeElapsed: Int = 0
    private var isStopWatchRunning = false

    private var updateTimer = Timer()
    private var stopwatchTimer = Timer()


    override fun onBind(intent: Intent): IBinder? {
        return null;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Notification 생성
        createChannel()
        getNotificationManager()

        // Activity로부터 Service가 해야하는 동작 가져오기
        val action = intent?.getStringExtra(STOPWATCH_ACTION)!!
        Log.d("Stopwatch", "onStartCommand Action: $action")

        // action에 따라 수행할 로직 구현하기
        when (action) {
            START -> startStopwatch()
            PAUSE -> pauseStopwatch()
            RESET -> resetStopwatch()
            GET_STATUS -> sendStatus()
            MOVE_TO_FOREGROUND -> moveToForeground()
            MOVE_TO_BACKGROUND -> moveToBackground()
        }

        return START_STICKY
    }

    // StopWatch 상태가 변경될 때 사용
    private fun sendStatus() {
        val statusIntent = Intent()
        statusIntent.action = STOPWATCH_STATUS
        statusIntent.putExtra(IS_STOPWATCH_RUNNING, isStopWatchRunning)
        statusIntent.putExtra(TIME_ELAPSED, timeElapsed)
        sendBroadcast(statusIntent)
    }

    // 1초마다 boradcast
    private fun startStopwatch() {
        isStopWatchRunning = true

        // stopwatch 상태 변경 알림
        sendStatus()

        stopwatchTimer = Timer()
        stopwatchTimer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                // Intent에 Action과 extrt를 설정하고 broadcast 한다.
                val stopwatchIntent = Intent()
                stopwatchIntent.action = STOPWATCH_TICK

                timeElapsed++
                _livetime.postValue(timeElapsed)

                stopwatchIntent.putExtra(TIME_ELAPSED, timeElapsed)
                sendBroadcast(stopwatchIntent)
            }
        }, 0, 1000)
    }

    private fun pauseStopwatch() {
        Log.d("Stopwatch", "pause")
        stopwatchTimer.cancel()
        isStopWatchRunning = false
        sendStatus()
    }

    private fun resetStopwatch() {
        Log.d("Stopwatch", "reset")
        pauseStopwatch()
        timeElapsed = 0
        sendStatus()
    }

    private fun moveToForeground() {
        if (isStopWatchRunning) {
            Log.d("Stopwatch", "startForeground ")

            startForeground(1, buildNotification())

            updateTimer = Timer()

            updateTimer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    updateNotification()

                }
            }, 0, 1000)
        }
    }

    private fun moveToBackground() {
        updateTimer.cancel()
        stopForeground(true)
    }


    // 26이상에서 Notification 시 필요
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Stopwatch",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.setSound(null, null)
            notificationChannel.setShowBadge(true)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationManager() {
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
    }

    private fun buildNotification(): Notification {
        val title = if (isStopWatchRunning) {
            "Stopwatch is running!"
        } else {
            "Stopwatch is paused!"
        }

        val hours: Int = timeElapsed.div(60).div(60)
        val minutes: Int = timeElapsed.div(60)
        val seconds: Int = timeElapsed.rem(60)

        val intent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setOngoing(true)
            .setContentText(
                "${"%02d".format(hours)}:${"%02d".format(minutes)}:${
                    "%02d".format(
                        seconds
                    )
                }"
            )
            .setColorized(true)
            .setColor(Color.parseColor("#BEAEE2"))
            .setSmallIcon(R.drawable.ic_play)
            .setOnlyAlertOnce(true)
            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun updateNotification() {
        notificationManager.notify(
            1,
            buildNotification()
        )
    }

}