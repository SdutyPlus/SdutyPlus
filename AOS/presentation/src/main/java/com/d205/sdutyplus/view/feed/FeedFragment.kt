package com.d205.sdutyplus.view.feed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.d205.domain.model.feed.HomeFeed
import com.d205.domain.model.mypage.Feed
import com.d205.domain.model.user.User
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentFeedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "FeedFragment"
@AndroidEntryPoint
class FeedFragment : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {

    private lateinit var feedAdapter: HomeFeedAdapter
    private lateinit var user: User
    private val feedViewModel: FeedViewModel by activityViewModels()

    override fun initOnViewCreated() {

        user = mainViewModel.user.value!!

        initAdapter()
        initView()
        initViewModel()
    }

    private fun initAdapter() {
        feedAdapter = HomeFeedAdapter(requireActivity())
        feedAdapter.apply {
            onClickStoryListener = object : HomeFeedAdapter.OnClickStoryListener{
                override fun onClick(homeFeed: HomeFeed) {
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

        binding.rvFeedList.apply {
            adapter = feedAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            this@FeedFragment.feedViewModel.homeFeeds.collectLatest {
                Log.d(TAG, "onTabSelected: collect $it")
                feedAdapter.submitData(it)
            }
        }
    }

    private fun moveToFeedCreateFragment() {
        findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToFeedCreateFragment())
    }
}