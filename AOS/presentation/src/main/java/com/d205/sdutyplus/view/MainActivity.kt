package com.d205.sdutyplus.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.d205.data.dao.UserSharedPreference
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseActivity
import com.d205.sdutyplus.databinding.ActivityMainBinding
import com.d205.sdutyplus.view.join.JoinViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    private val mainViewModel : MainViewModel by viewModels()

    override fun init() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        CoroutineScope(Dispatchers.Main).launch {
            getUser()
        }
    }

    private suspend fun getUser() {
        mainViewModel.getUser()
    }

}