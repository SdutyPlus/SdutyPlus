package com.d205.sdutyplus.view.login

import android.Manifest
import android.util.Log
import android.view.View
import com.d205.data.dao.UserSharedPreference
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseActivity
import com.d205.sdutyplus.databinding.ActivityLoginBinding
import com.d205.sdutyplus.utils.showToast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LoginActivity"

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val REQUIRED_PERMISSIONS = mutableListOf(
        Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.FOREGROUND_SERVICE).toTypedArray()
    private val userPref = UserSharedPreference(this)

    override fun init() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN    // Hide the status bar.

        initPermission()

        val jwt = userPref.getStringFromPreference("jwt")
        Log.d(TAG, "jwt : $jwt")

    }

    private fun initPermission() {
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                showToast("권한을 다시 설정해주세요!")
            }
        }

        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setPermissions(*REQUIRED_PERMISSIONS)
            .check()
    }
}
