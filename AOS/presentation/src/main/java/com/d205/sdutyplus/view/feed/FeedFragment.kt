package com.d205.sdutyplus.view.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentFeedBinding

class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    override fun init() {
        displayBottomNav(true)
        binding.apply {
            ivCreateStory.setOnClickListener {
                findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToFeedCreateFragment())
            }
        }
    }
}