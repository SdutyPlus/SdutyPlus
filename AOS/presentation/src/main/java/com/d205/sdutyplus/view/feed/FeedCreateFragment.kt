package com.d205.sdutyplus.view.feed

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentFeedCreateBinding
import com.d205.sdutyplus.utills.showToast
import com.d205.sdutyplus.view.common.LoadingDialogFragment
import com.d205.sdutyplus.view.feed.viewmodel.FeedCreateViewModel

class FeedCreateFragment : BaseFragment<FragmentFeedCreateBinding>(R.layout.fragment_feed_create) {
    private val feedCreateViewModel: FeedCreateViewModel by activityViewModels()
    private val loadingDialogFragment by lazy { LoadingDialogFragment() }

    override fun initOnViewCreated() {
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        feedCreateViewModel.clearState()
    }

    private fun initView() {
        initObserver()
        limitEditTextLength()

        binding.apply {
            vm = feedCreateViewModel
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            layoutAddImage.setOnClickListener {
                moveToFeedDecoFragment()
            }
            ivCreateFeed.setOnClickListener {
                if (isNotImageEmpty()) {
                    feedCreateViewModel.setContent(etWrite.text.toString())
                    feedCreateViewModel.createFeed()
                }
                else {
                    showToast("이미지를 추가해주세요!")
                }
            }
        }
    }

    private fun initObserver() {
        feedCreateViewModel.apply {
            isFeedCreated.observe(viewLifecycleOwner) { isFeedCreated ->
                if (isFeedCreated == true){
                    showToast("피드 생성을 성공했습니다!")
                    findNavController().popBackStack()
                }
            }
            isFeedCreateFailed.observe(viewLifecycleOwner) {
                if (it == true) {
                    showToast("피드 생성에 실패했습니다.")
                    setIsFeedCreateFailed(false)
                }
            }
            loadingFlag.observe(viewLifecycleOwner) {
                when(it) {
                    true -> showLoader()
                    false -> hideLoader()
                }
            }
        }
    }

    private fun isNotImageEmpty(): Boolean {
        if (feedCreateViewModel.image.value == null) {
            showToast("이미지를 추가해주세요!")
            return false
        }
        return true
    }

    private fun showToast(msg: String) {
        requireContext().showToast(msg)
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

    private fun hideLoader() {
        if(loadingDialogFragment.isAdded) {
            loadingDialogFragment.dismissAllowingStateLoss()
        }
    }

    private fun showLoader() {
        if(!loadingDialogFragment.isAdded) {
            loadingDialogFragment.show(parentFragmentManager, "loader")
        }
    }
}