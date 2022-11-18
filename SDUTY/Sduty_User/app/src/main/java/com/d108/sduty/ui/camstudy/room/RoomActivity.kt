package com.d108.sduty.ui.camstudy.room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.d108.sduty.R
import com.d108.sduty.common.EXTRA_IS_NEWLY_CREATED
import com.d108.sduty.common.EXTRA_ROOM_ID

class RoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
    }

    fun getStudyName(): String {
        return intent.getStringExtra("studyName") ?: throw IllegalStateException()
    }

    fun getRoomId(): String {
        return intent.getStringExtra(EXTRA_ROOM_ID) ?: throw IllegalStateException()
    }

    fun isNewlyCreated(): Boolean {
        return intent.getBooleanExtra(EXTRA_IS_NEWLY_CREATED, false)
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.nav_host_room).let {
            it?.childFragmentManager?.fragments?.firstOrNull()
        }

        if (currentFragment is GroupCallFragment) {
            // ignore event
        } else {
            super.onBackPressed()
        }
    }
}