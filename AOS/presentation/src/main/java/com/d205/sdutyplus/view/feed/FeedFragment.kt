package com.d205.sdutyplus.view.feed

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.d205.domain.model.feed.Feed
import com.d205.domain.model.feed.HomeFeed
import com.d205.domain.model.user.User
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentFeedBinding
import com.d205.sdutyplus.view.mypage.MyPageFragmentDirections
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
        initAdapter()
        initView()
        initViewModel()
    }

    private fun initAdapter() {
        feedAdapter = HomeFeedAdapter(requireActivity())
        feedAdapter.apply {
            onClickStoryListener = object : HomeFeedAdapter.OnClickStoryListener{
                override fun onClick(homeFeed: Feed) {
                    //feed click 시 이벤트
                    Log.d(TAG, "Feed Clicked! : $homeFeed")
                    moveToFeedDetailFragment(homeFeed)
                }
            }
        }
    }

    private fun initView() {

        binding.apply {
            layoutSwipeRefresh.setOnRefreshListener {
                feedAdapter.refresh()
                layoutSwipeRefresh.isRefreshing = false
            }
        }
        displayBottomNav(true)
        binding.ivCreateFeed.setOnClickListener {
            moveToFeedCreateFragment()
        }

        binding.rvFeedList.apply {
            adapter = feedAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun initViewModel() {
//        mainViewModel.user.observe(viewLifecycleOwner) {
//            if(it != null) {
//                user = mainViewModel.user.value!!
//            }
//        }

        lifecycleScope.launch {
            this@FeedFragment.feedViewModel.homeFeeds.collectLatest {
                feedAdapter.submitData(it)
            }
        }
    }

    private fun moveToFeedCreateFragment() {
        findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToFeedCreateFragment())
    }

    override fun onResume() {
        super.onResume()

        feedAdapter.refresh()
    }

    private fun moveToFeedDetailFragment(feed: Feed) {
        findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToFeedDetailFragment(feed))
    }
}