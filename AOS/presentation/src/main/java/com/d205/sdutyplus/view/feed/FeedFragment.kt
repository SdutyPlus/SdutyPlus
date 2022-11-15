package com.d205.sdutyplus.view.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.d205.domain.model.mypage.Feed
import com.d205.domain.model.user.User
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentFeedBinding
import kotlinx.coroutines.flow.collectLatest

class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    private lateinit var feedAdapter: FeedAdapter
    private lateinit var user: User
    private val feedViewModel: FeedViewModel by activityViewModels()

    override fun initOnViewCreated() {

        user = mainViewModel.user.value!!

        initAdapter()
        initView()
        initViewModel()
    }

    private fun initAdapter() {
        feedAdapter = FeedAdapter(requireActivity())
        feedAdapter.apply {
            onClickStoryListener = object : FeedAdapter.OnClickStoryListener{
                override fun onClick(feed: Feed) {
                    //feed click 시 이벤트
                }
            }
        }
    }

    private fun initView() {

        displayBottomNav(true)
        binding.ivCreateFeed.setOnClickListener {
            moveToFeedCreateFragment()
        }
    }

    private fun initViewModel() {
        this@FeedFragment.feedViewModel.apply {
            pagingFeedList.collectLatest {
                feedAdapter.submitData(this@FeedFragment.lifecycle, it)
            }
        }
    }

    private fun moveToFeedCreateFragment() {
        findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToFeedCreateFragment())
    }
}