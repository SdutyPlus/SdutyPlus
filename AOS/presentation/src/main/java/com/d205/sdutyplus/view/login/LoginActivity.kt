package com.d205.sdutyplus.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseActivity
import com.d205.sdutyplus.databinding.ActivityMainBinding

class LoginActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun init() {

    }
}