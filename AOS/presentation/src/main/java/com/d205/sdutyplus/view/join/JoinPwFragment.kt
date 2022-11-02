package com.d205.sdutyplus.view.join


import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentJoinPwBinding
import com.d205.sdutyplus.uitls.showToast
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "JoinPwFragment"

@AndroidEntryPoint
class JoinPwFragment : BaseFragment<FragmentJoinPwBinding>(R.layout.fragment_join_pw) {
    private val joinViewModel : JoinViewModel by viewModels()
    private val args by navArgs<JoinPwFragmentArgs>()

    override fun initOnViewCreated() {
        initView()
    }

    private fun initView() {
        binding.apply {
            joinViewModel = this@JoinPwFragment.joinViewModel

            etPasswordCheck.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    updateIsSameFlag()
                }
            })

            btnNext.setOnClickListener {
                if(this@JoinPwFragment.joinViewModel.isSamePassword.value!!) {
                    moveToJoinProfileFragment(userId = args.id, password = etPassword.text.toString())
                }
                else {
                    requireContext().showToast("비밀번호가 일치하지 않습니다!")
                }
            }
        }
    }

    private fun checkPasswordSame(): Boolean {
        return binding.etPassword.text.toString() == binding.etPasswordCheck.text.toString()
    }

    private fun updateIsSameFlag() {
        this@JoinPwFragment.joinViewModel.setPasswordSame(checkPasswordSame())
    }

    private fun moveToJoinProfileFragment(userId: String, password: String) {
        findNavController().navigate(
            JoinPwFragmentDirections.actionJoinPwFragmentToJoinProfileFragment(
                id = userId, pw = password
            ))
    }
}