package com.d205.sdutyplus.view.login

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.d205.data.dao.UserSharedPreference
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseActivity
import com.d205.sdutyplus.databinding.ActivityLoginBinding
import com.d205.sdutyplus.uitls.showToast
import com.d205.sdutyplus.view.MainActivity
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "LoginActivity"

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val REQUIRED_PERMISSIONS = mutableListOf(
        Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_NETWORK_STATE).toTypedArray()
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
