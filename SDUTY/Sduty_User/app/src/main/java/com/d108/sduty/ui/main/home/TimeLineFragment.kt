package com.d108.sduty.ui.main.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.d108.sduty.R
import com.d108.sduty.adapter.paging.TimeLinePagingAdapter
import com.d108.sduty.common.*
import com.d108.sduty.databinding.FragmentTimeLineBinding
import com.d108.sduty.model.dto.Follow
import com.d108.sduty.model.dto.Likes
import com.d108.sduty.model.dto.Scrap
import com.d108.sduty.model.dto.Timeline
import com.d108.sduty.ui.common.LoadingDialog
import com.d108.sduty.ui.main.home.dialog.BlockDialog
import com.d108.sduty.ui.main.home.dialog.PushInfoDialog
import com.d108.sduty.ui.main.mypage.setting.viewmodel.SettingViewModel
import com.d108.sduty.ui.sign.dialog.TagSelectOneFragment
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.ui.viewmodel.StoryViewModel
import com.d108.sduty.utils.*
import com.d108.sduty.utils.sharedpreference.FCMPreference
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

// 타임라인 - 게시글(스크롤 뷰), 게시글 쓰기, 닉네임(게시글 상세페이지 이동) 더보기(신고, 스크랩, 팔로잉) ,좋아요, 댓글, 필터, 데일리 질문, 추천 팔로우
private const val TAG ="TimeLineFragment"
class TimeLineFragment : Fragment(), PopupMenu.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener   {
    private lateinit var binding: FragmentTimeLineBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val storyViewModel: StoryViewModel by activityViewModels()
    private val settingViewModel: SettingViewModel by viewModels()

    private val loadingDialog: LoadingDialog by lazy { LoadingDialog() }

    private lateinit var pageAdapter: TimeLinePagingAdapter
    private lateinit var menuSelectedTimeline: Timeline
    private var mTagFlag = ALL_TAG
    private var mTagName = ""
    override fun onResume() {
        super.onResume()
        mainViewModel.displayBottomNav(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimeLineBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog.show(parentFragmentManager, null)
        initView()
        initViewModel()

    }

    override fun onRefresh() {
        getTimelineList()
        binding.swipeRefresh.isRefreshing = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView(){
        if(SettingsPreference().getFirstLoginCheck()){
            PushInfoDialog().let{
                it.show(parentFragmentManager, null)
                it.onClickConfirm = object :PushInfoDialog.OnClickConfirm{
                    override fun onClick(state: Int) {
                        SettingsPreference().setPushState(state)
                        getFirebaseToken()
                        requireContext().showToast("Push 설정이 저장되었습니다")
                    }
                }
            }
            SettingsPreference().setFirstLoginCheck(false)
        }
        getFirebaseToken()

        pageAdapter = TimeLinePagingAdapter(requireActivity(), requireContext())
        pageAdapter.apply {
            onClickTimelineItem = object : TimeLinePagingAdapter.TimeLineClickListener{
                override fun onFavoriteClicked(view: View, timeline: Timeline, position: Int) {
                    YoYo.with(Techniques.Tada)
                        .duration(180)
                        .repeat(3)
                        .playOn(view)
                    storyViewModel.likeStoryInTimeLine(Likes(mainViewModel.user.value!!.seq, timeline.story.seq))
                    changeLikes(position)
                }

                override fun onScrapClicked(timeline: Timeline, position: Int) {
                    storyViewModel.scrapStory(Scrap(mainViewModel.user.value!!.seq, timeline.story.seq))
                    changeScrap(position)
                }
                override fun onReplyClicked(timeline: Timeline) {
                    findNavController().safeNavigate(TimeLineFragmentDirections.actionTimeLineFragmentToStoryDetailFragment(timeline.story.seq))
                }
                override fun onMenuClicked(view: View, timeline: Timeline) {
                    menuSelectedTimeline = timeline
                    if(timeline.profile.userSeq == mainViewModel.user.value!!.seq){
                        PopupMenu(requireContext(), view).apply {
                            setOnMenuItemClickListener(this@TimeLineFragment)
                            inflate(R.menu.menu_story_own_writer)
                            menu[0].isVisible = false
                            show()
                        }
                    }else {
                        PopupMenu(requireContext(), view).apply {
                            setOnMenuItemClickListener(this@TimeLineFragment)
                            inflate(R.menu.menu_story_visiters)
                            if (mainViewModel.checkFollowUser(timeline.story.writerSeq)) {
                                menu[0].title = "언팔로우"
                            } else {
                                menu[0].title = "팔로우"
                            }
                            show()
                        }
                    }
                }
                override fun onProfileClicked(timeline: Timeline) {
                    if(timeline.profile.userSeq != mainViewModel.user.value!!.seq)
                        findNavController().safeNavigate(TimeLineFragmentDirections.actionTimeLineFragmentToUserProfileFragment(timeline.profile.userSeq))
                    else{
                        findNavController().safeNavigate(TimeLineFragmentDirections.actionTimeLineFragmentToMyPageFragment())
                    }
                }
            }
        }
        binding.apply {
            lifecycleOwner = this@TimeLineFragment
            swipeRefresh.setOnRefreshListener(this@TimeLineFragment)
            ivRegisterStory.setOnClickListener {
                findNavController().safeNavigate(
                    TimeLineFragmentDirections
                        .actionTimeLineFragmentToStoryRegisterFragment()
                )
            }

            recyclerTimeline.apply {
                adapter = pageAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
                    override fun onGlobalLayout() {
                    }
                })
            }
            ivFilter.setOnClickListener {
                TagSelectOneFragment(requireContext(), FLAG_TIMELINE).let{
                    it.show(parentFragmentManager, null)
                    it.onDismissDialogListener = object : TagSelectOneFragment.OnDismissDialogListener{
                        override fun onDismiss(tagName: String, flag: Int) {
                            mTagFlag = flag
                            mTagName = tagName
                            getTimelineList()
                        }
                    }
                }
            }
        }

    }

    private fun initViewModel(){
        storyViewModel.pagingAllTimelineList.observe(viewLifecycleOwner){
            pageAdapter.submitData(this.lifecycle, it)
            loadingDialog.dismiss()
        }
        storyViewModel.pagingTimelineJobAndAllList.observe(viewLifecycleOwner){
            pageAdapter.submitData(this.lifecycle, it)
        }
        storyViewModel.pagingTimelineInterestAndAllList.observe(viewLifecycleOwner){
            pageAdapter.submitData(this.lifecycle, it)
        }
        storyViewModel.pagingTimelineFollowList.observe(viewLifecycleOwner){
            pageAdapter.submitData(this.lifecycle, it)
        }


        storyViewModel.getAllTimelineListValue(mainViewModel.user.value!!.seq)
        mainViewModel.getProfileValue(mainViewModel.user.value!!.seq)
        storyViewModel.isFollowSucceed.observe(viewLifecycleOwner){
            mainViewModel.getProfileValue(mainViewModel.user.value!!.seq)
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item!!.itemId) {
            R.id.follow -> {
                storyViewModel.doFollow(Follow(mainViewModel.user.value!!.seq, menuSelectedTimeline.story.writerSeq))
                if (mainViewModel.checkFollowUser(menuSelectedTimeline.story.writerSeq)) {
                    item.title = "언팔로우"
                }else{
                    item.title = "팔로우"
                }
            }
            R.id.story_delete ->{
                BlockDialog(FLAG_DELETE).let {
                    it.onClickConfirmListener = object : BlockDialog.OnClickConfirmListener{
                        override fun onClick() {
                            storyViewModel.deleteStory(mainViewModel.user.value!!.seq, menuSelectedTimeline.story)
                        }
                    }
                    it.show(parentFragmentManager, null)
                }
            }
            R.id.block -> {
                BlockDialog(FLAG_BLOCK).let {
                    it.onClickConfirmListener = object : BlockDialog.OnClickConfirmListener{
                        override fun onClick() {
                            storyViewModel.blockStory(mainViewModel.user.value!!.seq, menuSelectedTimeline.story)
                        }
                    }
                    it.show(parentFragmentManager, null)
                }
            }
            R.id.report ->{
                BlockDialog(FLAG_REPORT).let {
                    it.onClickConfirmListener = object : BlockDialog.OnClickConfirmListener{
                        override fun onClick() {
                            storyViewModel.reportStory(menuSelectedTimeline.story)
                        }
                    }
                    it.show(parentFragmentManager, null)
                }
            }
        }
        return true
    }


    private fun getFirebaseToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
            if (!it.isSuccessful) {
                Log.d(TAG, "onCreate: FCM 토큰 얻기 실패", it.exception)
                return@OnCompleteListener
            }else{
                Log.d(TAG, "getFirebaseToken: ${it.result}")
                FCMPreference().setFcmToken(it.result)
            }
            settingViewModel.updateFCMToken(mainViewModel.user.value!!)

        })
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotiChannel("sduty_id", "sduty")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotiChannel(channelId: String, channelName: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)

        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun getTimelineList(){
        Log.d(TAG, "getTimelineList: ${mTagFlag}  ,  ${mTagName}")
        when(mTagFlag){
            JOB_TAG -> {
                storyViewModel.getTimelineJobAndAllList(mainViewModel.user.value!!.seq, mTagName)
            }
            INTEREST_TAG -> {
                storyViewModel.getTimelineInterestAndAllList(mainViewModel.user.value!!.seq, mTagName)
            }
            ALL_TAG -> {
                storyViewModel.getAllTimelineListValue(mainViewModel.user.value!!.seq)
            }
        }
    }
}
