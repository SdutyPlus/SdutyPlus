package com.d205.sdutyplus.view.login

import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentLoginBinding
import com.d205.sdutyplus.uitls.KAKAO_JOIN
import com.d205.sdutyplus.uitls.NAVER_JOIN
import com.d205.sdutyplus.uitls.showToast
import com.d205.sdutyplus.view.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

private const val TAG = "LoginFragment"

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val userApiClient = UserApiClient.instance
    private lateinit var userAccessToken: String
    private val loginViewModel: LoginViewModel by viewModels()

    override fun initOnViewCreated() {
        getKakaoKeyHash()
        initNaverLogin()
        initView()
    }

    private fun getKakaoKeyHash() {
        val kakaoKeyHash = Utility.getKeyHash(context = requireContext())
        Log.d(TAG, "init: $kakaoKeyHash")
    }

    private fun initNaverLogin() {
        val naverClientId = getString(R.string.naver_client_id)
        val naverClientSecret = getString(R.string.naver_client_secret)
        val naverClientName = getString(R.string.naver_client_name)
        NaverIdLoginSDK.initialize(requireContext(), naverClientId, naverClientSecret , naverClientName)
    }

    private fun initView() {
        binding.apply {
            btnKakaoLogin.setOnClickListener {
                startKakaoLogin()
            }
            btnNaverLogin.setOnClickListener {
                startNaverLogin()
            }
            btnJoin.setOnClickListener {
                moveToJoinIdFragment()
            }
        }
    }

    private fun startKakaoLogin() {
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

    private val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { kakoToken, error ->
        if (kakoToken != null) {
            Log.d(TAG, "카카오계정 로그인 성공 token : ${kakoToken.accessToken}")
            userAccessToken = kakoToken.accessToken

            // 사용자 정보 가져오기
            userApiClient.me { userInfo, error ->
                if (error != null) {
                    Log.d(TAG, "카카오계정 사용자 정보 가져오기 실패")
                } else if (userInfo != null) {
                    Log.d(
                        TAG,
                        "카카오계정 사용자 정보 가져오기 성공\n" +
                                "닉네임 = ${userInfo.kakaoAccount?.profile?.nickname}\n " +
                                "프사 : ${userInfo.kakaoAccount?.profile?.profileImageUrl}\n" +
                                "이메일 : ${userInfo.kakaoAccount?.email}\n" +
                                "아이디 : ${userInfo.id}\n" +
                                "이름 : ${userInfo.kakaoAccount?.name}"
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        if(isJoinedKakaoUser(userAccessToken)) {
                            loginViewModel.user.collect {
                                if(it is ResultState.Success) {
                                    mainViewModel.setUserValue(it.data)
                                    moveToMainActivity()
                                }
                            }
                            //mainViewModel.setUserValue(loginViewModel.user.data)

                        }
                        else {
                            withContext(Dispatchers.Main) {
                                moveToJoinProfileFragment(KAKAO_JOIN)
                            }
                            //moveToJoinProfileFragment(KAKAO_JOIN)
                        }
                    }
                }
            }
        }
        else if (error != null) {
            Log.d(TAG, error.toString())
            requireContext().showToast("다시 로그인해주세요.")
        }
    }

    private fun startNaverLogin(){
        var naverToken :String? = ""

        val nidProfileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                Log.d(TAG, "onSuccess: id: ${response.profile} \n" +
                        "token: $naverToken")

                userAccessToken = naverToken!!
                requireContext().showToast("네이버 아이디 로그인 성공!")

                CoroutineScope(Dispatchers.IO).launch {
                    if(isJoinedNaverUser(userAccessToken)) {
//                        mainViewModel.setUserValue(loginViewModel.user.value!!)
//                        moveToMainActivity()
                        loginViewModel.user.collect {
                            if(it is ResultState.Success) {
                                mainViewModel.setUserValue(it.data)
                                moveToMainActivity()
                            }
                        }
                    }
                    else {
                        withContext(Dispatchers.Main) {
                            moveToJoinProfileFragment(NAVER_JOIN)
                        }
                    }
                }
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                requireContext().showToast("errorCode: ${errorCode}\n" +
                        "errorDescription: $errorDescription")
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        /** OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다. */
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(nidProfileCallback)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

                requireContext().showToast("errorCode: ${errorCode}\n" +
                        "errorDescription: $errorDescription")
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(requireContext(), oauthLoginCallback)
    }

    private suspend fun isJoinedKakaoUser(kakaoToken: String): Boolean {
        withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
            loginViewModel.kakaoLogin(kakaoToken)
        }
        return loginViewModel.isLoginSucceed.value!!
    }


    private suspend fun isJoinedNaverUser(naverToken: String): Boolean {
        withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
            loginViewModel.naverLogin(naverToken)
        }
        return loginViewModel.isLoginSucceed.value!!
    }

    fun moveToMainActivity() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    fun moveToJoinProfileFragment(socialType: Int) {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToJoinProfileFragment(socialType = socialType, userAccessToken))
    }

    private fun moveToJoinIdFragment() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToJoinIdFragment())
    }
}