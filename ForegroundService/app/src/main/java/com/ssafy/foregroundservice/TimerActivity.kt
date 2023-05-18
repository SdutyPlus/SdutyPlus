package com.ssafy.foregroundservice

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import com.ssafy.foregroundservice.StopwatchService.Companion.STOPWATCH_TICK
import com.ssafy.foregroundservice.StopwatchService.Companion.TIME_ELAPSED
import com.ssafy.foregroundservice.databinding.ActivityTimerBinding
import java.util.*

class TimerActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTimerBinding

    private var isStopwatchRunning = false

    // Service와 Broadcast로 통신할 시 사용
    private lateinit var statusReceiver: BroadcastReceiver
    private lateinit var timeReceiver: BroadcastReceiver

    // Service가 동작 시 다른 컴포넌트도 동작하는지 확인하기 위한 Activity Timer
    private var t = 0
    val timer : Timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initButton()

        // Foreground Service 실행 시 activity로직 정상 동작 확인
        activityTiemrRun()

        // Service의 데이터를 Livedata로 받기
        checkSerivceDataByLiveData()

    }

    override fun onStart() {
        super.onStart()

        // Activity가 보이면 Servie를 Background로 전환
        moveToBackground()
    }

    override fun onResume() {
        super.onResume()

        getStopwatchStatus()

        // Service로 부터 Broadcast로 데이터 전달 받기
        setBroadcastReceiver()
    }

    // Activity 멈추면 receiver 제거
    override fun onPause() {
        super.onPause()

        unregisterReceiver(statusReceiver)
        unregisterReceiver(timeReceiver)

        // Service Foreground로 전환
        moveToForeground()
    }

    override fun onDestroy() {
        super.onDestroy()

        timer.cancel()
    }



    private fun initButton() {
        binding.apply {
            toggleButton.setOnClickListener {
                if (isStopwatchRunning) pauseStopwatch() else startStopwatch()
            }

            resetImageView.setOnClickListener {
                resetStopwatch()
            }
        }
    }

    private fun activityTiemrRun() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                t++
                Log.d("Stopwatch", "activity: $t")

                runOnUiThread {
                    binding.tvActivitTime.text = "activit time : $t"
                }
            }
        }, 0, 1000)
    }

    private fun checkSerivceDataByLiveData() {
        StopwatchService.livetime.observe(this@TimerActivity) {
            binding.tvServiceLivetime.text = "ServiceLivetime : $it"
        }
    }

    private fun setBroadcastReceiver() {
        // 서비스로 부터 Watch 상태 수신
        val statusFilter = IntentFilter()
        statusFilter.addAction(StopwatchService.STOPWATCH_STATUS)
        statusReceiver = object : BroadcastReceiver() {
            @SuppressLint("SetTextI18n")
            override fun onReceive(p0: Context?, p1: Intent?) {
                val isRunning = p1?.getBooleanExtra(StopwatchService.IS_STOPWATCH_RUNNING, false)!!
                isStopwatchRunning = isRunning
                val timeElapsed = p1.getIntExtra(StopwatchService.TIME_ELAPSED, 0)

                updateLayout(isStopwatchRunning)
                updateStopwatchValue(timeElapsed)
            }
        }
        registerReceiver(statusReceiver, statusFilter)

        // 서비스로 부터 Time 값 수신
        val timeFilter = IntentFilter()
        timeFilter.addAction(StopwatchService.STOPWATCH_TICK)
        timeReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                val timeElapsed = p1?.getIntExtra(StopwatchService.TIME_ELAPSED, 0)!!
                updateStopwatchValue(timeElapsed)
            }
        }
        registerReceiver(timeReceiver, timeFilter)
    }

    private fun updateStopwatchValue(timeElapsed: Int) {
        val hours: Int = (timeElapsed / 60) / 60
        val minutes: Int = timeElapsed / 60
        val seconds: Int = timeElapsed % 60
        binding.stopwatchValueTextView.text =
            "${"%02d".format(hours)}:${"%02d".format(minutes)}:${"%02d".format(seconds)}"
    }

    private fun updateLayout(isStopwatchRunning: Boolean) {
        if (isStopwatchRunning) {
            binding.toggleButton.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_stop)
            binding.resetImageView.visibility = View.INVISIBLE
        } else {
            binding.toggleButton.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_play)
            binding.resetImageView.visibility = View.VISIBLE
        }
    }

    private fun getStopwatchStatus() {
        val stopwatchService = Intent(this, StopwatchService::class.java)
        stopwatchService.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.GET_STATUS)
        startService(stopwatchService)
    }

    private fun startStopwatch() {
        val stopwatchService = Intent(this, StopwatchService::class.java)
        stopwatchService.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.START)
        startService(stopwatchService)
    }

    private fun pauseStopwatch() {
        val stopwatchService = Intent(this, StopwatchService::class.java)
        stopwatchService.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.PAUSE)
        startService(stopwatchService)
    }

    private fun resetStopwatch() {
        val stopwatchService = Intent(this, StopwatchService::class.java)
        stopwatchService.putExtra(StopwatchService.STOPWATCH_ACTION, StopwatchService.RESET)
        startService(stopwatchService)
    }

    private fun moveToForeground() {
        val stopwatchService = Intent(this, StopwatchService::class.java)
        stopwatchService.putExtra(
            StopwatchService.STOPWATCH_ACTION,
            StopwatchService.MOVE_TO_FOREGROUND
        )
        startService(stopwatchService)
    }

    private fun moveToBackground() {
        val stopwatchService = Intent(this, StopwatchService::class.java)
        stopwatchService.putExtra(
            StopwatchService.STOPWATCH_ACTION,
            StopwatchService.MOVE_TO_BACKGROUND
        )
        startService(stopwatchService)
    }
}