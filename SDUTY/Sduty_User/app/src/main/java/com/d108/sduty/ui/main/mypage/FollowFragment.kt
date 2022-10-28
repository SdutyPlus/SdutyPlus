package com.d108.sduty.ui.main.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.d108.sduty.adapter.FollowAdapter
import com.d108.sduty.common.FLAG_FOLLOWEE
import com.d108.sduty.common.FLAG_FOLLOWER
import com.d108.sduty.databinding.FragmentFollowBinding
import com.d108.sduty.model.dto.Follow
import com.d108.sduty.ui.main.mypage.viewmodel.FollowViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.ui.viewmodel.StoryViewModel
import com.d108.sduty.utils.safeNavigate
import com.google.android.material.tabs.TabLayout

private const val TAG ="FollowFragment"
class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private val viewModel: FollowViewModel by viewModels()
    private val storyViewModel: StoryViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val args: FollowFragmentArgs by navArgs()
    private lateinit var followAdapter: FollowAdapter
    var flagTab = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
    }

    private fun initViewModel(){
        viewModel.apply {
            if(args.flag == FLAG_FOLLOWER){
                binding.tabFollow.selectTab(binding.tabFollow.getTabAt(0))
                flagTab = FLAG_FOLLOWER
                getFollower(args.userSeq)
            }else{
                binding.tabFollow.selectTab(binding.tabFollow.getTabAt(1))
                flagTab = FLAG_FOLLOWER
                getFollowee(args.userSeq)

            }
            followerList.observe(viewLifecycleOwner){
                followAdapter.tabFlag = FLAG_FOLLOWER
                followAdapter.list = followerList.value!!
                setMyFollowPage(false)
                binding.tabFollow.getTabAt(0)!!.text = "팔로워 ${it.size}명"
            }
            followeeList.observe(viewLifecycleOwner){
                binding.tabFollow.getTabAt(1)!!.text = "팔로잉 ${it.size}명"
                if(args.userSeq == mainViewModel.user.value!!.seq){
                    setMyFollowPage(true)
                }else{
                    setMyFollowPage(false)
                }
                followAdapter.tabFlag = FLAG_FOLLOWEE
                followAdapter.list = followeeList.value!!
            }
        }
        storyViewModel.isFollowSucceed.observe(viewLifecycleOwner){
            mainViewModel.getProfileValue(mainViewModel.profile.value!!.userSeq)
        }
        mainViewModel.profile.observe(viewLifecycleOwner){
            when(flagTab){
                FLAG_FOLLOWER -> viewModel.getFollower(args.userSeq)
                FLAG_FOLLOWEE -> viewModel.getFollowee(args.userSeq)
            }
        }
    }

    private fun initView(){
        followAdapter = FollowAdapter(mainViewModel.user.value!!.seq, mainViewModel.profile.value!!)
        followAdapter.apply {
            onClickFollowListener = object : FollowAdapter.OnClickFollowListener{
                override fun onClickFollowBtn(follow: Follow) {
                    findNavController().safeNavigate(FollowFragmentDirections.actionFollowFragmentToUserProfileFragment(follow.profile!!.userSeq))
                }
                override fun onClickProfile(follow: Follow) {
                    findNavController().safeNavigate(FollowFragmentDirections.actionFollowFragmentToUserProfileFragment(follow.profile!!.userSeq))
                }
            }
        }
        binding.apply {
            tabFollow.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        0 -> {
                            viewModel.getFollower(args.userSeq)
                        }
                        1 -> {
                            viewModel.getFollowee(args.userSeq)
                        }
                    }
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    when(tab!!.position){
                        0 -> {
                            viewModel.getFollower(args.userSeq)
                        }
                        1 -> {
                            viewModel.getFollowee(args.userSeq)
                        }
                    }
                }
            })
            recyclerFollow.apply {
                adapter = followAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            commonTopBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}