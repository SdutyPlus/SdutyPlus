package com.d205.sdutyplus.view.login

import android.content.Intent
import android.util.Log
import androidx.navigation.fragment.findNavController
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

private const val TAG = "LoginFragment"
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {
    private val userApiClient = UserApiClient.instance
    private lateinit var userToken: String

    override fun init() {
        initKakao()
        initNaver()
        initView()
    }

    private fun initKakao() {
        val keyHash = Utility.getKeyHash(context = requireContext())
        Log.d(TAG, "init: $keyHash")
    }

    private fun initNaver() {
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

    private val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (token != null) {
            Log.d(TAG, "카카오계정 로그인 성공 token : ${token.accessToken}")
            userToken = token.accessToken

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
                    if(isJoinedUser(token.accessToken)) {
                        moveToMainActivity()
                    }
                    else {
                        moveToJoinProfileFragment(KAKAO_JOIN)
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

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                Log.d(TAG, "onSuccess: id: ${response.profile} \n" +
                        "token: $naverToken")

                userToken = naverToken!!
                requireContext().showToast("네이버 아이디 로그인 성공!")

                if(isJoinedUser(userToken)) {
                    moveToMainActivity()
                }
                else {
                    moveToJoinProfileFragment(NAVER_JOIN)
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
                NidOAuthLogin().callProfileApi(profileCallback)
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

    private fun isJoinedUser(token: String): Boolean {
        return false
    }

    fun moveToMainActivity() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
    }

    fun moveToJoinProfileFragment(route: Int) {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToJoinProfileFragment(route = route, userToken))
    }

    private fun moveToJoinIdFragment() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToJoinIdFragment())
    }
}