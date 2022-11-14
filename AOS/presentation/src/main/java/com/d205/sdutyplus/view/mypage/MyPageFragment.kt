package com.d205.sdutyplus.view.mypage

import android.net.Uri
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.d205.domain.model.mypage.Feed
import com.d205.domain.model.user.User
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentMyPageBinding
import com.d205.sdutyplus.view.feed.FeedAdapter
import com.d205.sdutyplus.view.feed.FeedViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MyPageFragment"

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val args by navArgs<MyPageFragmentArgs>()
    private lateinit var user: User
    private val feedViewModel : FeedViewModel by viewModels()
    private lateinit var feedAdapter: FeedAdapter

    override fun initOnViewCreated() {
//        val tmp = args.qwe
//        Log.d(TAG, "init: $tmp")
        user = getUser()


        lifecycleScope.launch {
            initAdapter()
            initViewModel()
            initView()
        }
    }

    private fun getUser() = mainViewModel.user.value!!

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@MyPageFragment
            feedVM = this@MyPageFragment.feedViewModel
            mainVM = this@MyPageFragment.mainViewModel

            tabMyPage.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab!!.position) {
                        0 -> {
                            //feedViewModel.getUserFeeds()
                        }
                        1 -> {
                            //feedViewModel.getScrapStoryList(mainViewModel.user.value!!.seq)
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    when (tab!!.position) {
                        0 -> {
                            //feedViewModel.getUserFeeds()
                        }
                        1 -> {
                            //feedViewModel.getScrapStoryList(mainViewModel.user.value!!.seq)
                        }
                    }
                }
            })
            tabMyPage.getTabAt(0)!!.select()
            recylerStory.apply {
                adapter = feedAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
            }

            ivSetting.setOnClickListener {
                findNavController().navigate(MyPageFragmentDirections.actionMypageFragmentToStatisticFragment())
            }
        }
    }

    private fun initAdapter() {
        feedAdapter = FeedAdapter(requireActivity())
        feedAdapter.apply {
            onClickStoryListener = object : FeedAdapter.OnClickStoryListener{
                override fun onClick(feed: Feed) {
                    // Feed Detail Fragment로 이동
                }
            }
        }
    }

    private suspend fun initViewModel() {
        feedViewModel.apply {
            getUserFeeds()
            pagingFeedList.collectLatest {
                feedAdapter.submitData(this@MyPageFragment.lifecycle, it)
            }
        }
    }
}