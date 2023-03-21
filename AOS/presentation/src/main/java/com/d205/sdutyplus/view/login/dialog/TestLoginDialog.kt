package com.d205.sdutyplus.view.login.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.d205.sdutyplus.databinding.DialogConfirmBinding
import com.d205.sdutyplus.databinding.DialogTestLoginBinding
import com.d205.sdutyplus.utils.showToast
import com.d205.sdutyplus.view.MainActivity
import com.d205.sdutyplus.view.common.LoadingDialogFragment
import com.d205.sdutyplus.view.login.LoginFragmentDirections
import com.d205.sdutyplus.view.login.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.launch

class TestLoginDialog : DialogFragment() {
    private lateinit var binding: DialogTestLoginBinding
    private val loginViewModel: LoginViewModel by activityViewModels()
    private val loadingDialogFragment: LoadingDialogFragment by lazy {
        LoadingDialogFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTestLoginBinding.inflate(inflater, container, false)

        initDialogWindow()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initbtn()
    }

    private fun initDialogWindow() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    private fun initObserver() {
        loginViewModel.loadingFlag.observe(viewLifecycleOwner) {
            when(it) {
                true -> showLoader()
                false -> hideLoader()
            }
        }
    }

    private fun initbtn() {
        binding.apply {
            btnAgree.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    loginViewModel.testLogin();

                    if(isLoginSucceeded()) { // 로그인 성공
                        if(isJoinedUser()) { // 이미 프로필 있는지?
                            // Main Activity로 이동
                            moveToMainActivity()
                        } else {
                            // profile Fragment로 이동
                            moveToJoinProfileFragment(3)
                        }
                    } else { // 로그인 실패
                        requireContext().showToast("테스트 계정 로그인에 실패했습니다.")
                    }

                    dismiss()
                }
            }

            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun hideLoader() {
        if(loadingDialogFragment.isAdded) {
            loadingDialogFragment.dismissAllowingStateLoss()
        }
    }

    private fun showLoader() {
        // 프래그먼트가 최근에 Activity에 더해진 적이 있으면 반환
        if(!loadingDialogFragment.isAdded) {
            loadingDialogFragment.show(parentFragmentManager, "loader")
        }
    }

    private fun isLoginSucceeded() = this@TestLoginDialog.loginViewModel.isLoginSucceeded
    private fun isJoinedUser(): Boolean = this@TestLoginDialog.loginViewModel.isJoinedUser()

    fun moveToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    fun moveToJoinProfileFragment(socialType: Int) {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToJoinProfileFragment(socialType = socialType, ""))
    }

}