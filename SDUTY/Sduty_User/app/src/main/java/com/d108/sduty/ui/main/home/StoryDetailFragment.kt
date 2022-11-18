package com.d108.sduty.ui.main.home

import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import androidx.annotation.IdRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.d108.sduty.R
import com.d108.sduty.adapter.ReplyAdapter
import com.d108.sduty.common.FLAG_BLOCK
import com.d108.sduty.common.FLAG_DELETE
import com.d108.sduty.common.FLAG_REPORT
import com.d108.sduty.common.NOT_PROFILE
import com.d108.sduty.databinding.FragmentStoryDetailBinding
import com.d108.sduty.model.dto.*
import com.d108.sduty.ui.main.home.dialog.BlockDialog
import com.d108.sduty.ui.sign.dialog.TagSelectDialog
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.ui.viewmodel.StoryViewModel
import com.d108.sduty.utils.*
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

// 게시글 상세 - 게시글 사진, 더보기, 좋아요, 댓글 등록, 조회, 스크랩
private const val TAG ="StoryDetailFragment"
class StoryDetailFragment : Fragment(), PopupMenu.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener  {
    private lateinit var binding: FragmentStoryDetailBinding
    private val viewModel: StoryViewModel by viewModels()
    private val args: StoryDetailFragmentArgs by navArgs()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var replyAdapter: ReplyAdapter
    private lateinit var selectedReply: Reply

    override fun onResume() {
        super.onResume()
        mainViewModel.displayBottomNav(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoryDetailBinding.inflate(inflater, container, false)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        val displaymetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displaymetrics)
        val deviceWidth = displaymetrics.widthPixels - convertDpToPx(requireContext(), 24f)
        val deviceHeight = deviceWidth / 3 * 4
        binding.apply {
            ivTimelineContent.layoutParams.width = deviceWidth
            ivTimelineContent.layoutParams.height = deviceHeight
            lifecycleOwner = this@StoryDetailFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()

    }

    override fun onRefresh() {
        viewModel.getTimelineValue(args.seq, mainViewModel.user.value!!.seq)
        binding.swipeRefresh.isRefreshing = false
    }

    private fun initViewModel() {
        viewModel.apply {
            getTimelineValue(args.seq, mainViewModel.user.value!!.seq)
            timeLine.observe(viewLifecycleOwner){
                replyAdapter.list = timeLine.value!!.replies
                binding.vm = viewModel
            }
            replyList.observe(viewLifecycleOwner){
                replyAdapter.list = it
            }
            isFollowSucceed.observe(viewLifecycleOwner){
                mainViewModel.getProfileValue(mainViewModel.user.value!!.seq)
            }
        }
        mainViewModel.apply {
            getProfileValue(mainViewModel.user.value!!.seq)
        }

    }

    private fun initView(){
        binding.swipeRefresh.setOnRefreshListener(this)
        replyAdapter = ReplyAdapter(mainViewModel.user.value!!.seq)
        replyAdapter.onClickReplyListener = object : ReplyAdapter.OnClickReplyListener{
            override fun onClick(view: View, position: Int) {
                selectedReply = replyAdapter.list[position]
                PopupMenu(requireContext(), view).apply {
                    setOnMenuItemClickListener(this@StoryDetailFragment)
                    inflate(R.menu.menu_reply)
                    show()
                }
            }
            override fun onClickProfile(userSeq: Int) {
                if(userSeq == mainViewModel.user.value!!.seq){
                    findNavController().safeNavigate(StoryDetailFragmentDirections.actionStoryDetailFragmentToMyPageFragment())
                }else {
                    findNavController().safeNavigate(StoryDetailFragmentDirections.actionStoryDetailFragmentToUserProfileFragment(userSeq))
                }
            }
        }
        binding.apply {
            vm = viewModel
            tvRegister.setOnClickListener {
                if(etReply.text.isEmpty()){
                    requireContext().showToast("내용을 입력해 주세요")
                    return@setOnClickListener
                }
                viewModel.insertReply(Reply(args.seq, mainViewModel.user.value!!.seq, etReply.text.toString()))
                etReply.setText("")
            }
            recyclerReply.apply {
                adapter = replyAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
            }
            ivFavorite.setOnClickListener {
                YoYo.with(Techniques.Tada)
                    .duration(180)
                    .repeat(3)
                    .playOn(it)
                viewModel.likeStory(Likes(mainViewModel.user.value!!.seq, args.seq), mainViewModel.user.value!!.seq)
            }
            ivScrap.setOnClickListener {
                viewModel.scrapStory(Scrap(mainViewModel.user.value!!.seq, args.seq))
            }
            ivMenu.setOnClickListener {
                @IdRes var menuId = 0
                if(viewModel.timeLine.value!!.story.writerSeq == mainViewModel.user.value!!.seq){
                    menuId = R.menu.menu_story_own_writer
                }else{
                    menuId = R.menu.menu_story_visiters
                }

                PopupMenu(requireContext(), it).apply {
                    setOnMenuItemClickListener(this@StoryDetailFragment)
                    inflate(menuId)

                    if (menuId == R.menu.menu_story_visiters && mainViewModel.checkFollowUser(viewModel.timeLine.value!!.story.writerSeq)) {
                        menu[0].title = "언팔로우"
                    }else if(menuId == R.menu.menu_story_visiters){
                        menu[0].title = "팔로우"
                    }
                    show()
                }
            }
            commonTopBack.setOnClickListener {
                navigateBack(requireActivity())
            }
            constProfileTop.setOnClickListener {
                if(mainViewModel.user.value!!.seq == viewModel.timeLine.value!!.story.writerSeq)
                    findNavController().safeNavigate(StoryDetailFragmentDirections.actionStoryDetailFragmentToMyPageFragment())
                else{
                    findNavController().safeNavigate(StoryDetailFragmentDirections.actionStoryDetailFragmentToUserProfileFragment(viewModel.timeLine.value!!.story.writerSeq))
                }
            }

        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.story_delete -> {
                requireActivity().showAlertDialog(
                    "삭제",
                    "스토리를 삭제하시겠습니까?",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            viewModel.deleteStory(mainViewModel.user.value!!.seq, viewModel.timeLine.value!!.story)
                            requireContext().showToast("삭제 되었습니다.")
                            findNavController().popBackStack()
                        }
                    })
            }
            R.id.story_update_tag -> {
                TagSelectDialog(requireContext()).let {
                    it.arguments = bundleOf("flag" to NOT_PROFILE)
                    it.onClickConfirm = object : TagSelectDialog.OnClickConfirm {
                        override fun onClick(
                            selectedJobList: JobHashtag?,
                            selectedInterestList: MutableList<InterestHashtag>
                        ) {
                            var story = viewModel.timeLine.value!!.story
                            var list = mutableListOf<Int>()
                            for (item in selectedInterestList) {
                                list.add(item.seq)
                            }
                            story.interestHashtag = list
                            viewModel.updateStory(story)
                        }
                    }
                    it.show(parentFragmentManager, null)
                }
            }
            R.id.follow -> {
                viewModel.doFollow(Follow(mainViewModel.user.value!!.seq, viewModel.timeLine.value!!.story.writerSeq))
            }
            R.id.report ->{
                BlockDialog(FLAG_REPORT).let {
                    it.onClickConfirmListener = object : BlockDialog.OnClickConfirmListener{
                        override fun onClick() {
                            viewModel.reportStory(viewModel.timeLine.value!!.story)
                            findNavController().popBackStack()
                        }
                    }
                    it.show(parentFragmentManager, null)
                }
            }
            R.id.block ->{
                BlockDialog(FLAG_BLOCK).let {
                    it.onClickConfirmListener = object : BlockDialog.OnClickConfirmListener{
                        override fun onClick() {
                            viewModel.blockStory(mainViewModel.user.value!!.seq, viewModel.timeLine.value!!.story)
                            findNavController().popBackStack()
                        }
                    }
                    it.show(parentFragmentManager, null)
                }
            }
            R.id.reply_delete ->{
                BlockDialog(FLAG_DELETE).let {
                    it.onClickConfirmListener = object :BlockDialog.OnClickConfirmListener{
                        override fun onClick() {
                            viewModel.deleteReply(selectedReply)
                        }
                    }
                    it.show(parentFragmentManager, null)
                }
            }
        }

        return true
    }
}
