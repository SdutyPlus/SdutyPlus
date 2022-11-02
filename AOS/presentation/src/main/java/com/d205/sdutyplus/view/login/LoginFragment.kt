package com.d205.sdutyplus.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentLoginBinding
import com.d205.sdutyplus.view.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

private const val TAG = "LoginFragment"
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val userApiClient = UserApiClient.instance

    override fun init() {
        val keyHash = Utility.getKeyHash(context = requireContext())
        Log.d(TAG, "init: $keyHash")

        binding.btnKakaoLogin.setOnClickListener {
            getKaKaoToken()
        }
    }
    private fun getKaKaoToken() {
        if (userApiClient.isKakaoTalkLoginAvailable(context = requireContext())) {
            Log.d(TAG, "카카오톡으로 로그인 가능")
            userApiClient.loginWithKakaoTalk(
                requireContext(),
                callback = kakaoLoginCallback
            )
        } else {
            Log.d(TAG, "카카오톡으로 로그인 불가능")
            userApiClient.loginWithKakaoAccount(
                requireContext(),
                callback = kakaoLoginCallback
            )
        }
    }

    private val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (token != null) {
            Log.d(TAG, "카카오계정 로그인 성공 token : ${token.accessToken}")

            // 사용자 정보 가져오기
            userApiClient.me { user, error ->
                if (error != null) {
                    Log.d(TAG, "카카오계정 사용자 정보 가져오기 실패")
                } else if (user != null) {
                    Log.d(
                        TAG,
                        "카카오계정 사용자 정보 가져오기 성공\n" +
                                "닉네임 = ${user.kakaoAccount?.profile?.nickname}\n " +
                                "프사 : ${user.kakaoAccount?.profile?.profileImageUrl}\n" +
                                "이메일 : ${user.kakaoAccount?.email}\n" +
                                "아이디 : ${user.id}\n" +
                                "이름 : ${user.kakaoAccount?.name}"
                    )
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                }
            }
        } else if (error != null) {
            Log.d(TAG, error.toString())
            Toast.makeText(requireContext(), "다시 로그인해주세요", Toast.LENGTH_SHORT).show()
        }
    }

}