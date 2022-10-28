package com.d205.sdutyplus.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.d205.sdutyplus.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}