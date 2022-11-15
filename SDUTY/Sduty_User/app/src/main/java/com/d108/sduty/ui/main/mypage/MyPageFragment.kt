package com.d108.sduty.ui.main.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.d108.sduty.adapter.ContributionAdapter
import com.d108.sduty.adapter.paging.StoryPagingAdapter
import com.d108.sduty.common.FLAG_FOLLOWEE
import com.d108.sduty.common.FLAG_FOLLOWER
import com.d108.sduty.common.MODIFY
import com.d108.sduty.databinding.FragmentMyPageBinding
import com.d108.sduty.model.dto.Story
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.ui.viewmodel.StoryViewModel
import com.d108.sduty.utils.safeNavigate
import com.google.android.material.tabs.TabLayout

// 마이페이지 - 내 닉네임, 프로필 사진, 숫자 표시(게시물, 팔로우, 팔로워), 한줄소개, 프로필 편집, 통계, 잔디그래프, 탭(내 게시물/ 스크랩), 내 게시물(그리드+스크롤) , 설정, 업적
private const val TAG ="MyPageFragment"
class MyPageFragment : Fragment(){
    private lateinit var binding: FragmentMyPageBinding
    private val viewModel: StoryViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var contributionAdapter: ContributionAdapter
    private lateinit var storyAdapter: StoryPagingAdapter

    override fun onResume() {
        super.onResume()
        mainViewModel.displayBottomNav(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyPageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.apply {
            getUserStoryList(mainViewModel.user.value!!.seq, mainViewModel.user.value!!.seq)
            pagingStoryList.observe(viewLifecycleOwner){
                storyAdapter.submitData(this@MyPageFragment.lifecycle, it)
            }
            pagingScrapList.observe(viewLifecycleOwner){
                storyAdapter.submitData(this@MyPageFragment.lifecycle, it)
            }
            contributionList.observe(viewLifecycleOwner){
                contributionAdapter.list = it
            }
            if(viewModel.contributionList.value == null) {
                getContribution(mainViewModel.user.value!!.seq)
            }
            getProfileValue(mainViewModel.user.value!!.seq)

        }
    }

    private fun initView(){
        contributionAdapter = ContributionAdapter()
        storyAdapter = StoryPagingAdapter(requireActivity())
        storyAdapter.apply {
            onClickStoryListener = object : StoryPagingAdapter.OnClickStoryListener{
                override fun onClick(story: Story) {
                    findNavController().safeNavigate(MyPageFragmentDirections.actionMyPageFragmentToStoryDetailFragment(story.seq))
                }
            }
        }
        binding.apply {
            lifecycleOwner = this@MyPageFragment
            vm = viewModel
            tabMyPage.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        0 -> {
                            viewModel.getUserStoryList(mainViewModel.user.value!!.seq, mainViewModel.user.value!!.seq)
                        }
                        1 -> {
                            viewModel.getScrapStoryList(mainViewModel.user.value!!.seq)
                        }
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        0 -> {
                            viewModel.getUserStoryList(mainViewModel.user.value!!.seq, mainViewModel.user.value!!.seq)
                        }
                        1 -> {
                            viewModel.getScrapStoryList(mainViewModel.user.value!!.seq)
                        }
                    }
                }
            })
            tabMyPage.getTabAt(0)!!.select()
            recylerStory.apply {
                adapter = storyAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
            }
            recylerContribution.apply {
                adapter = contributionAdapter
                layoutManager = GridLayoutManager(requireContext(), 7, GridLayoutManager.HORIZONTAL, false)
            }
            ivSetting.setOnClickListener {
                findNavController().safeNavigate(MyPageFragmentDirections.actionMyPageFragmentToSettingFragment())
            }
            tvCountFollow.setOnClickListener {
                findNavController().safeNavigate(MyPageFragmentDirections.actionMyPageFragmentToFollowFragment(mainViewModel.user.value!!.seq, FLAG_FOLLOWER))
            }
            tvCountFollower.setOnClickListener {
                findNavController().safeNavigate(MyPageFragmentDirections.actionMyPageFragmentToFollowFragment(mainViewModel.user.value!!.seq, FLAG_FOLLOWEE))
            }
            btnEditProfile.setOnClickListener {
                findNavController().safeNavigate(MyPageFragmentDirections.actionMyPageFragmentToProfileRegistFragment(MODIFY))
            }
        }
    }
}
