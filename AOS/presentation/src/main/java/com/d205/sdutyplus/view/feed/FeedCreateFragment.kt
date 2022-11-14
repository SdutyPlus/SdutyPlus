package com.d205.sdutyplus.view.feed

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentFeedCreateBinding
import com.d205.sdutyplus.uitls.showToast
import com.d205.sdutyplus.view.feed.viewmodel.FeedCreateViewModel

class FeedCreateFragment : BaseFragment<FragmentFeedCreateBinding>(R.layout.fragment_feed_create) {
    private val feedCreateViewModel: FeedCreateViewModel by activityViewModels()

    override fun initOnViewCreated() {
        initView()
    }

    private fun initView() {
        limitEditTextLength()

        binding.apply {
            vm = feedCreateViewModel
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            layoutAddImage.setOnClickListener {
                moveToFeedDecoFragment()
            }
        }
    }

    private fun limitEditTextLength() {
        binding.apply {
            etWrite.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    tvWordLength.text = "${etWrite.length()} / 200"
                    if (etWrite.length() > 200) {
                        //requireContext().showToast("최대 200자까지 입력 가능합니다.")
                        etWrite.setTextColor(Color.RED)
                        tvWordLength.setTextColor(Color.RED)
                    }
                    else {
                        etWrite.setTextColor(Color.BLACK)
                        tvWordLength.setTextColor(Color.BLACK)
                    }
                }
            })
        }
    }

    private fun moveToFeedDecoFragment() {
        findNavController().navigate(FeedCreateFragmentDirections.actionFeedCreateFragmentToFeedDecoFragment())
    }
}