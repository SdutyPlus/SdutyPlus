package com.d205.sdutyplus.view.login

import android.content.Intent
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentLoginBinding
import com.d205.sdutyplus.utils.KAKAO_JOIN
import com.d205.sdutyplus.utils.NAVER_JOIN
import com.d205.sdutyplus.utils.showToast
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        Log.d(TAG, "init kakao: $kakaoKeyHash")
    }

    private fun initNaverLogin() {
        val naverClientId = getString(R.string.naver_client_id)
        val naverClientSecret = getString(R.string.naver_client_secret)
        val naverClientName = getString(R.string.naver_client_name)
        NaverIdLoginSDK.initialize(requireContext(), naverClientId, naverClientSecret , naverClientName)
    }

    private fun initView() {
        binding.apply {
            // 카카오 로그인 버튼
            btnKakaoLogin.setOnClickListener {
                startKakaoLogin()
            }
            // 네이버 로그인 버튼
            btnNaverLogin.setOnClickListener {
                startNaverLogin()
            }
        }
    }

    private fun startKakaoLogin() {
        // 카카오톡 설치 여부 체크    true : 카카오톡 설치 되어있음, false : 미설치
        if (isKakaoTalkInstalled()) {
            Log.d(TAG, "카카오톡 앱이 설치 돼있음")
//            userApiClient.loginWithKakaoTalk(
//                requireContext(),
//                callback = kakaoLoginCallback
//            )
            userApiClient.loginWithKakaoAccount(
                requireContext(),
                callback = kakaoLoginCallback
            )
        } else {
            Log.d(TAG, "카카오톡 앱이 설치 안 돼있음")
            userApiClient.loginWithKakaoAccount(
                requireContext(),
                callback = kakaoLoginCallback
            )
        }
    }

    private fun isKakaoTalkInstalled(): Boolean =
        userApiClient.isKakaoTalkLoginAvailable(context = requireContext())

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

                    CoroutineScope(Dispatchers.Main).launch {
                        signInKakaoUser(userAccessToken)

                        // 해당 유저 회원가입 여부 체크     true : 회원가입한 유저, false : 신규 유저
                        if(isJoinedKakaoUser()) {
                            moveToMainActivity()
                        }
                        else {
                            if(isLoginSucceeded()) {
                                moveToJoinProfileFragment(KAKAO_JOIN)
                            }
                            else {
                                showToast("소셜 로그인에 실패했습니다.")
                            }
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

                CoroutineScope(Dispatchers.Main).launch {
                    signInNaverUser(userAccessToken)

                    // 해당 유저 회원가입 여부 체크     true : 회원가입한 유저, false : 신규 유저
                    if(isJoinedNaverUser()) {
                        moveToMainActivity()
                    }
                    else {
                        if(isLoginSucceeded()) {
                            moveToJoinProfileFragment(NAVER_JOIN)
                        }
                        else {
                            showToast("소셜 로그인에 실패했습니다.")
                        }
                    }
                }
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.d(TAG,"errorCode: ${errorCode}\n" +
                        "errorDescription: $errorDescription")
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        /* OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나
         * NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다. */
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 토큰을 받아온다.
                naverToken = NaverIdLoginSDK.getAccessToken()

                //로그인 유저 정보를 가져온다.
                NidOAuthLogin().callProfileApi(nidProfileCallback)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()

                Log.d(TAG,"errorCode: ${errorCode}\n" +
                        "errorDescription: $errorDescription")
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(requireContext(), oauthLoginCallback)
    }

    private suspend fun signInKakaoUser(kakaoToken: String) {
        loginViewModel.kakaoLogin(kakaoToken)
    }

    private fun isJoinedKakaoUser(): Boolean = this@LoginFragment.loginViewModel.isJoinedUser()

    private suspend fun signInNaverUser(naverToken: String) {
        loginViewModel.naverLogin(naverToken)
    }

    private fun isJoinedNaverUser(): Boolean = this@LoginFragment.loginViewModel.isJoinedUser()
    private fun isLoginSucceeded() = this@LoginFragment.loginViewModel.isLoginSucceeded

    private fun showToast(msg: String) {
        requireContext().showToast(msg)
    }

    fun moveToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun moveToJoinProfileFragment(socialType: Int) {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToJoinProfileFragment(socialType = socialType, userAccessToken))
    }

    private fun moveToJoinIdFragment() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToJoinIdFragment())
    }
}