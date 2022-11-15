package com.d205.sdutyplus.view.login

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
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

private const val TAG = "LoginActivity"

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val REQUIRED_PERMISSIONS = mutableListOf(
        Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_NETWORK_STATE).toTypedArray()

    private val sha1 = arrayListOf<Byte>(
        0xB5.toByte(), 0x54.toByte(), 0xD7.toByte(), 0x62.toByte(), 0x77.toByte(), 0xE6.toByte(),
        0x72.toByte(), 0x1C.toByte(), 0x4F.toByte(), 0xFF.toByte(),
        0xBA.toByte(), 0xCA.toByte(), 0x34.toByte(), 0xF8.toByte(), 0x50.toByte(), 0x3A.toByte(), 0x3F.toByte(),
        0x8A.toByte(), 0x58.toByte(), 0x0D.toByte())

    private val tmp1 = ByteArray(20)

    override fun init() {
        initPermission()
        val pref = UserSharedPreference(this)
        Log.d(TAG, "sharedPreference jwt : ${pref.getStringFromPreference("jwt")}")
        for(i in 0..19) {
            tmp1[i] = sha1[i]
        }
        Log.d("encode keyhash : ", Base64.encodeToString(tmp1, Base64.NO_WRAP));
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
