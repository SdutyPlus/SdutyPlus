package com.d205.sdutyplus.view.join

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentJoinIdBinding
import com.d205.sdutyplus.utills.showToast
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "JoinIdFragment"

@AndroidEntryPoint
class JoinIdFragment : BaseFragment<FragmentJoinIdBinding>(R.layout.fragment_join_id) {
    private val joinViewModel : JoinViewModel by viewModels()

    override fun initOnViewCreated() {
        initView()
    }

    private fun initView() {
        binding.apply {
            joinViewModel = this@JoinIdFragment.joinViewModel
            lifecycleOwner = this@JoinIdFragment

            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    updateCorrectIdFlag()
                }
            })

            btnNext.setOnClickListener {
                updateUsedIdFlag()

                if(isIdUsed()) {
                    requireContext().showToast("이미 존재하는 아이디입니다.")
                }
                else {
                    val userId = binding.etEmail.text.toString()
                    moveToJoinPwFragment(userId)
                }
            }
        }
    }

    private fun moveToJoinPwFragment(userId: String) {
        findNavController().navigate(
            JoinIdFragmentDirections.actionJoinIdFragmentToJoinPwFragment(id = userId))
    }

    private fun isIdUsed(): Boolean = this@JoinIdFragment.joinViewModel.isUsedId.value!!

    private fun updateUsedIdFlag() {
        this@JoinIdFragment.joinViewModel.checkUsedId(binding.etEmail.text.toString())
    }

    private fun updateCorrectIdFlag() {
        this@JoinIdFragment.joinViewModel.checkIdPatternCorrect(binding.etEmail.text.toString())
    }
}