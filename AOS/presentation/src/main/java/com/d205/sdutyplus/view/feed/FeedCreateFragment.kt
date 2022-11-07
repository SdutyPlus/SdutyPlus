package com.d205.sdutyplus.view.feed

import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentFeedCreateBinding

class FeedCreateFragment : BaseFragment<FragmentFeedCreateBinding>(R.layout.fragment_feed_create) {

    override fun initOnViewCreated() {
        initView()
    }

    private fun initView() {
        binding.apply {
            tvAddImg.setOnClickListener {
                moveToFeedDecoFragment()
            }
        }
    }

    private fun moveToFeedDecoFragment() {
        findNavController().navigate(FeedCreateFragmentDirections.actionFeedCreateFragmentToFeedDecoFragment())
    }
}