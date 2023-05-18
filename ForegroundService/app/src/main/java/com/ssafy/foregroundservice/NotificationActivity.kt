package com.ssafy.foregroundservice

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

private const val CHANNEL_ID2 = "Timer"

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)



        val btn : Button = findViewById(R.id.btn_send_notify)
        btn.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                RequestNotificationPermission()
            }

            createNotificationChannel()
            val builder: NotificationCompat.Builder = buildNotification()
            with(NotificationManagerCompat.from(this)) {
                notify(1, builder.build())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun RequestNotificationPermission() {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    Toast.makeText(this@NotificationActivity, "Notify", Toast.LENGTH_SHORT).show()
                }
                override fun onPermissionDenied(deniedPermissions: List<String>) {
                    Toast.makeText(this@NotificationActivity,
                        "권한을 허가해주세요.",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(Manifest.permission.POST_NOTIFICATIONS)
            .check()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "TimerChannel"
            val descriptionText = "Timer를 위한 Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID2, name, importance).apply {
                description = descriptionText

            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification() : NotificationCompat.Builder {
        val builder : NotificationCompat.Builder
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  {
            // 26이상에서 Channel 필요
            builder = NotificationCompat.Builder(this, CHANNEL_ID2)
        } else {
            builder = NotificationCompat.Builder(this)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle("TimerTitle")
            .setContentText("TimerContent")

        return builder
    }
}