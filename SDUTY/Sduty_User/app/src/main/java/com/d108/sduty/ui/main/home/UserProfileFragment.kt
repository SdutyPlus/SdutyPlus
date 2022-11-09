package com.d108.sduty.ui.main.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.d108.sduty.R
import com.d108.sduty.adapter.ContributionAdapter
import com.d108.sduty.adapter.paging.StoryPagingAdapter
import com.d108.sduty.common.FLAG_FOLLOWEE
import com.d108.sduty.common.FLAG_FOLLOWER
import com.d108.sduty.databinding.FragmentUserProfileBinding
import com.d108.sduty.model.dto.Follow
import com.d108.sduty.model.dto.Story
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.ui.viewmodel.StoryViewModel
import com.d108.sduty.utils.safeNavigate

//사용자 프로필 - 사용자 닉네임, 프로필 사진, 숫자 표시(게시물, 팔로우, 팔로워), 한줄소개,
// 잔디그래프,게시물(그리드+스크롤) , 더 보기, 업적
private const val TAG = "UserProfileFragment"
class UserProfileFragment : Fragment(), PopupMenu.OnMenuItemClickListener{
    private lateinit var binding: FragmentUserProfileBinding
    private val viewModel: StoryViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val args: UserProfileFragmentArgs by navArgs()
    private lateinit var storyAdapter: StoryPagingAdapter
    private lateinit var contributionAdapter: ContributionAdapter

    override fun onResume() {
        super.onResume()
        mainViewModel.displayBottomNav(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
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
            getContribution(args.userSeq)
            getUserStoryList(mainViewModel.user.value!!.seq, args.userSeq)

            pagingStoryList.observe(viewLifecycleOwner){
                storyAdapter.submitData(this@UserProfileFragment.lifecycle, it)
            }

            isFollowSucceed.observe(viewLifecycleOwner) {
                mainViewModel.getProfileValue(mainViewModel.user.value!!.seq)
                viewModel.getProfileValue(args.userSeq)
            }
            contributionList.observe(viewLifecycleOwner){
                contributionAdapter.list = it
            }
            profile.observe(viewLifecycleOwner){
                binding.apply {
                    vm = viewModel
                    mainVM = mainViewModel
                }
            }
        }
    }

    private fun initView() {
        contributionAdapter = ContributionAdapter()
        storyAdapter = StoryPagingAdapter(requireActivity())
        storyAdapter.onClickStoryListener = object : StoryPagingAdapter.OnClickStoryListener{
            override fun onClick(story: Story) {
                findNavController().safeNavigate(UserProfileFragmentDirections.actionUserProfileFragmentToStoryDetailFragment(story.seq))
            }
        }
        binding.apply {
            vm = viewModel
            mainVM = mainViewModel
            recylerStory.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = storyAdapter
            }
            btnFollow.setOnClickListener {
                viewModel.doFollow(Follow(mainViewModel.user.value!!.seq, viewModel.profile.value!!.userSeq))
                mainViewModel.getProfileValue(mainViewModel.user.value!!.seq)
            }

            ivSetting.setOnClickListener {
                PopupMenu(requireContext(), it).apply {
                    setOnMenuItemClickListener(this@UserProfileFragment)
                    inflate(R.menu.menu_story_visiters)
                    if (mainViewModel.checkFollowUser(viewModel.profile.value!!.userSeq)) {
                        menu[0].title = "언팔로우"
                    }else{
                        menu[0].title = "팔로우"
                    }
                    menu[1].isVisible = false
                    menu[2].isVisible = false
                    show()
                }
            }
            tvCountFollow.setOnClickListener {
                findNavController().safeNavigate(UserProfileFragmentDirections.actionUserProfileFragmentToFollowFragment(args.userSeq, FLAG_FOLLOWER))
            }
            tvCountFollower.setOnClickListener {
                findNavController().safeNavigate(UserProfileFragmentDirections.actionUserProfileFragmentToFollowFragment(args.userSeq, FLAG_FOLLOWEE))
            }
            recylerContribution.apply {
                layoutManager = GridLayoutManager(requireContext(), 7, GridLayoutManager.HORIZONTAL, false)
                adapter = contributionAdapter
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.follow -> {
                viewModel.doFollow(Follow(mainViewModel.user.value!!.seq, viewModel.profile.value!!.userSeq))
                mainViewModel.getProfileValue(mainViewModel.user.value!!.seq)
                if (mainViewModel.checkFollowUser(viewModel.profile.value!!.userSeq)) {
                    item.title = "언팔로우"
                }else{
                    item.title = "팔로우"
                }
            }
        }
        return true
    }
}
