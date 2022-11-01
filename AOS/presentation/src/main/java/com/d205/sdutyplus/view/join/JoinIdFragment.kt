package com.d205.sdutyplus.view.join

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentJoinIdBinding
import com.d205.sdutyplus.uitls.showToast
import java.util.regex.Pattern

private const val TAG = "JoinIdFragment"
class JoinIdFragment : BaseFragment<FragmentJoinIdBinding>(R.layout.fragment_join_id) {
    private val joinViewModel : JoinViewModel by viewModels()

    override fun init() {
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
                    this@JoinIdFragment.joinViewModel.checkIdCorrect(binding.etEmail.text.toString())
                }
            })

            btnNext.setOnClickListener {
                this@JoinIdFragment.joinViewModel.checkUsedId(binding.etEmail.text.toString())

                if(this@JoinIdFragment.joinViewModel.isUsedId.value!!) {
                    requireContext().showToast("이미 존재하는 아이디입니다.")
                }
                else {
                    val userId = binding.etEmail.text.toString()
                    findNavController().navigate(
                        JoinIdFragmentDirections.actionJoinIdFragmentToJoinPwFragment(id = userId))
                }
            }
        }
    }

}