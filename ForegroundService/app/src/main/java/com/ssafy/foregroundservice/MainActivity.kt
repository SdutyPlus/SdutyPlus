package com.ssafy.foregroundservice




import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.ssafy.foregroundservice.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnMoveToNotify.setOnClickListener {
                startActivity(Intent(this@MainActivity, NotificationActivity::class.java))
            }
            btnMoveToTimer.setOnClickListener {
                startActivity(Intent(this@MainActivity, TimerActivity::class.java))
            }
        }

    }

}

