package com.d108.sduty.ui.main.mypage.setting

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d108.sduty.databinding.FragmentSettingBinding
import com.d108.sduty.ui.MainActivity
import com.d108.sduty.ui.main.mypage.setting.viewmodel.SettingViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.SettingsPreference
import com.d108.sduty.utils.safeNavigate
import com.d108.sduty.utils.showAlertDialog
import com.d108.sduty.utils.showToast
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback


private const val TAG = "SettingFragment"
class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val viewModel: SettingViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        binding.apply {
            commonTopBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnPushAll.setOnCheckedChangeListener { buttonView, isChecked ->
                when(isChecked){
                    true -> {
                        btnPushPersonal.isChecked = true
                        updatePushState()
                    }
                    else -> {}
                }
            }
            btnPushPersonal.setOnCheckedChangeListener { buttonView, isChecked ->
                when(isChecked){
                    false -> {
                        btnPushAll.isChecked = false
                        updatePushState()
                    }
                    else -> {}
                }
            }

            btnLogout.setOnClickListener {
                requireActivity().showAlertDialog("로그아웃","로그아웃 하시겠습니까?", object : DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        naverLogout()
                        kakaoLogout()
                        clearActivity()
                    }
                })

            }
            btnLock.setOnClickListener {
                findNavController().safeNavigate(SettingFragmentDirections.actionSettingFragmentToAppLockFragment())
            }
            btnAutologin.isChecked = SettingsPreference().getAutoLoginState()
            btnAutologin.setOnCheckedChangeListener { buttonView, isChecked ->
                SettingsPreference().setAutoLoginState(isChecked)
                if(!isChecked){
                    SettingsPreference().setUserId("")
                }
            }

            btnResign.setOnClickListener{
                requireActivity().showAlertDialog("회원 탈퇴","정말로 탈퇴하시겠습니까?\n복구 할 수 없습니다.", object :DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        when(which){
                            DialogInterface.BUTTON_POSITIVE -> {
                                viewModel.resignUser(mainViewModel.user.value!!)
                                naverDisconnect()
                                kakaoDisconnect()
                            }
                        }
                    }
                })
            }

            btnAsk.setOnClickListener{
                findNavController().safeNavigate(SettingFragmentDirections.actionSettingFragmentToQuestionListFragment())
            }
            btnNotice.setOnClickListener {
                findNavController().safeNavigate(SettingFragmentDirections.actionSettingFragmentToNoticeFragment())
            }
        }

        }

    private fun initViewModel(){
        viewModel.isSucceedResign.observe(viewLifecycleOwner){
            when(it){
                true -> {
                    requireActivity().showAlertDialog("회원 탈퇴", "탈퇴가 완료되었습니다.", null)
                    clearActivity()
                }
                else -> {}
            }
        }
    }


    private fun clearActivity(){


        SettingsPreference().setUserId("")
        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    private fun kakaoLogout(){
        UserApiClient.instance.logout{
            if (it != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", it)
            }
            else {
                Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }
    private fun kakaoDisconnect(){
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(TAG, "연결 끊기 실패", error)
            }
            else {
                Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
    }
    private fun naverLogout(){
        NaverIdLoginSDK.logout()
    }

    private fun naverDisconnect(){
        NidOAuthLogin().callDeleteTokenApi(requireContext(), object : OAuthLoginCallback {
            override fun onSuccess() {
                //서버에서 토큰 삭제에 성공한 상태입니다.
            }
            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d(TAG, "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d(TAG, "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }
            override fun onError(errorCode: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                onFailure(errorCode, message)
            }
        })
    }


    private fun updatePushState(){
        binding.apply {
            if(btnPushAll.isChecked){
                SettingsPreference().setPushState(2)
            }else if(!btnPushAll.isChecked && btnPushPersonal.isChecked){
                SettingsPreference().setPushState(1)
            }else if(!btnPushAll.isChecked && !btnPushPersonal.isChecked){
                SettingsPreference().setPushState(0)
            }
        }
    }




}