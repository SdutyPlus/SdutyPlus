package com.d108.sduty.ui.sign

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d108.sduty.common.*
import com.d108.sduty.databinding.FragmentLoginBinding
import com.d108.sduty.ui.sign.dialog.DialogFindInfo
import com.d108.sduty.ui.sign.dialog.PermissionDialog
import com.d108.sduty.ui.sign.dialog.RegisterDialog
import com.d108.sduty.ui.sign.viewmodel.LoginViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.SettingsPreference
import com.d108.sduty.utils.safeNavigate
import com.d108.sduty.utils.showAlertDialog
import com.d108.sduty.utils.showToast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import java.util.regex.Pattern

//첫화면 - 로그인 / ID, PW 입력, 로그인 , 카카오, 네이버 로그인, 아이디/비밀번호 찾기, 회원가입 하기
private const val TAG ="LoginFragment"
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(SettingsPreference().getAutoLoginState() && SettingsPreference().getUserId() != ""){
            viewModel.getUserInfo(SettingsPreference().getUserId())
        }
        initViewModel()
        initView()
    }


    private fun initViewModel(){
        mainViewModel.isRegisterProfile.observe(viewLifecycleOwner){
            when(it){
                true -> {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTimeLineFragment())
                    requireContext().showToast("${mainViewModel.user.value!!.name}님 반갑습니다.")
                }
                false -> {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToProfileRegistFragment(REGISTER))
                    requireContext().showToast("프로필 등록 후 이용해 주세요")
                }
            }
        }

        viewModel.apply {
            user.observe(viewLifecycleOwner) {
                mainViewModel.setUserValue(it)
                if(SettingsPreference().getAutoLoginState()){
                    SettingsPreference().setUserId(it.id)
                }
                mainViewModel.getProfileValue(it.seq)
            }
            isLoginSucceed.observe(viewLifecycleOwner){
                when(it){
                    false -> requireContext().showToast("아이디와 비밀번호를 확인해 주세요")
                    true -> {

                    }
                }
            }
            isExistKakaoAccount.observe(viewLifecycleOwner){
                when(it){
                    false -> {
                        val listener = DialogInterface.OnClickListener { _, _ ->
                            findNavController().safeNavigate(LoginFragmentDirections.actionLoginFragmentToTermsFragment(
                                KAKAO_JOIN, viewModel.token.value.toString()))
                        }
                        requireActivity().showAlertDialog("", "가입되지 않은 계정입니다. 가입 하시겠습니까?", listener)
                    }
                    else -> {}
                }
            }
            isExistNaverAccount.observe(viewLifecycleOwner){
                when(it){
                    false -> {
                        val listener = DialogInterface.OnClickListener { _, _ ->
                            findNavController().safeNavigate(LoginFragmentDirections.actionLoginFragmentToTermsFragment(
                                NAVER_JOIN, viewModel.token.value.toString()))
                        }
                        requireActivity().showAlertDialog("", "가입되지 않은 계정입니다. 가입 하시겠습니까?", listener)
                    }
                    else -> {}
                }
            }
            getJobListValue()
        }
    }

    private fun initView() {

        if(SettingsPreference().getFirstRunCheck()){
            SettingsPreference().setFirstRunCheck(false)
            PermissionDialog(requireContext()).show(parentFragmentManager, null)
        }


        binding.apply {
            btnLogin.setOnClickListener {
                val id = binding.etId.text.toString()
                val pw = binding.etPw.text.toString()
                if(id.isEmpty() || pw.isEmpty()){
                    requireContext().showToast("아이디와 비밀번호를 모두 입력해 주세요.")
                }else{
                    viewModel.login(id, pw)
                }
            }
            btnKakaoLogin.setOnClickListener {
                kakaoLogin()
            }
            btnNaverLogin.setOnClickListener {
                naverLogin()
            }
            btnJoin.setOnClickListener {
                // 원래 코드 : JoinFragment로 이동
//                findNavController().safeNavigate(LoginFragmentDirections.actionLoginFragmentToJoinFragment(
//                    COMMON_JOIN))
                // 수정 : RegisterDialog 띄우기
                openRegisterDialog()
            }

            var filter = arrayOf(InputFilter{src, start, end, dst, dstart, dend ->
                val ps = Pattern.compile("^[a-zA-Z0-9]+\$")
                if(!ps.matcher(src).matches()){
                    return@InputFilter ""
                }else{
                    return@InputFilter null
                }
            }, InputFilter.LengthFilter(15))
            etId.filters = filter

            filter = arrayOf(InputFilter{src, start, end, dst, dstart, dend ->
                val ps = Pattern.compile("^[a-zA-Z0-9!@#$%^&*]+\$")
                if(!ps.matcher(src).matches()){
                    return@InputFilter ""
                }else{
                    return@InputFilter null
                }
            }, InputFilter.LengthFilter(20))

            etPw.filters = filter

            tvFindId.setOnClickListener{
                DialogFindInfo(requireContext()).let {
                    it.arguments = bundleOf("flag" to FIND_ID)
                    it.show(parentFragmentManager.beginTransaction(),null)
                }
            }
            tvFindPw.setOnClickListener{
                DialogFindInfo(requireContext()).let {
                    it.arguments = bundleOf("flag" to FIND_PW)
                    it.show(parentFragmentManager.beginTransaction(),null)
                }
            }
        }
    }

    private fun naverLogin() {
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                viewModel.naverLogin(NaverIdLoginSDK.getAccessToken()!!)
                viewModel.setAccessToken(NaverIdLoginSDK.getAccessToken()!!)
            }
            override fun onFailure(httpStatus: Int, message: String) {
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(requireContext(), oauthLoginCallback)
    }


    private fun kakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                viewModel.kakaoLogin(token.accessToken)
                viewModel.setAccessToken(token.accessToken)
            }
        }
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
                } else if (token != null) {
                    viewModel.kakaoLogin(token.accessToken)
                    viewModel.setAccessToken(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }
    }

    // 회원가입으로 이동하는 dialog (기존 JoinFragment를 다이얼로그로)
    private fun openRegisterDialog() {
        RegisterDialog().let {
            // 클릭 리스너 정의
            it.setOnClickListener(object : RegisterDialog.OnDialogClickListener {
                override fun onClicked(joinType: Int) {
                    findNavController().safeNavigate(LoginFragmentDirections.actionLoginFragmentToTermsFragment(joinType))
                    it.dismiss()
                }
            })
            it.show(parentFragmentManager, null)
        }
    }

    private fun checkPermission(){
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
            }
            override fun onPermissionDenied(deniedPermissions: List<String>) {
                requireActivity().showToast("필수 권한을 허용해야 이용이 가능합니다.")
                requireActivity().finish()
            }

        }
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .check()
    }
}
