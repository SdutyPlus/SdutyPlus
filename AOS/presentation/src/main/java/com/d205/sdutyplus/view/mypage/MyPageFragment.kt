package com.d205.sdutyplus.view.mypage

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.d205.data.dao.UserSharedPreference
import com.d205.domain.model.mypage.Feed
import com.d205.domain.model.user.User
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentMyPageBinding
import com.d205.sdutyplus.uitls.showToast
import com.d205.sdutyplus.view.MainActivity
import com.d205.sdutyplus.view.MainViewModel
import com.d205.sdutyplus.view.feed.FeedAdapter
import com.d205.sdutyplus.view.feed.FeedViewModel
import com.d205.sdutyplus.view.login.LoginActivity
import com.google.android.material.tabs.TabLayout
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.NidOAuthPreferencesManager
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

private const val TAG = "MyPageFragment"

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    @Inject lateinit var userSharedPreference: UserSharedPreference

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
            //initViewModel()
            initView()
        }
    }

    private fun getUser() = mainViewModel.user.value!!

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@MyPageFragment
            feedVM = this@MyPageFragment.feedViewModel
            mainVM = this@MyPageFragment.mainViewModel

            tvDelete.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setMessage("탈퇴하시겠습니까?")
                    .setPositiveButton("네", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface, p1: Int) {
                            CoroutineScope(Dispatchers.IO).launch {
                                this@MyPageFragment.mainViewModel.deleteUser()
                                if(mainViewModel.isDeletedSuccess) {
                                    if(getSocialType() == "kakao") {
                                        Log.d(TAG, "카카오 회원 탈퇴 진행 socialType : ${getSocialType()}")
                                        kakaoUnlink()
                                    }
                                    else {
                                        Log.d(TAG, "네이버 회원 탈퇴 진행 socialType : ${getSocialType()}")
                                        naverUnlink()
                                    }
                                }
                                else {
                                    Log.d(TAG, "회원 탈퇴 실패")
                                    withContext(Dispatchers.Main) {
                                        showToast("회원 탈퇴 실패")
                                        moveToLoginActivity()
                                    }
                                }
                            }
                        }
                    })
                    .setNegativeButton("아니요", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface, p1: Int) {
                            p0.dismiss()
                        }
                    }).show()
            }

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

            if(user.imgUrl != null) {
                ivProfile.setImageURI(Uri.parse(user.imgUrl))
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

    private fun getSocialType() = userSharedPreference.getStringFromPreference("socialType")

    fun moveToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun kakaoUnlink() {
        UserApiClient.instance.unlink { error ->
            if(error != null) {
                Log.d(TAG, "카카오 계정 삭제 실패!")
            }
            else {
                Log.d(TAG, "카카오 계정 삭제 성공!")
                showToast("카카오 회원 탈퇴 성공")
                userSharedPreference.removeFromPreference("jwt")
                moveToLoginActivity()
            }
        }
    }

    private fun naverUnlink() {
        NidOAuthLogin().callDeleteTokenApi(requireContext(), object : OAuthLoginCallback {
            override fun onSuccess() {
                //서버에서 토큰 삭제에 성공한 상태입니다.
                showToast("네이버 회원 탈퇴 성공")
                userSharedPreference.removeFromPreference("jwt")
                moveToLoginActivity()
            }
            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d(TAG, "naver 탈퇴 errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d(TAG, "naver 탈퇴 errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
                showToast("회원 탈퇴 실패")
                moveToLoginActivity()
            }
            override fun onError(errorCode: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                showToast("회원 탈퇴 실패")
                moveToLoginActivity()
                onFailure(errorCode, message)
            }
        })
    }

    private fun showToast(msg: String) {
        requireContext().showToast(msg)
    }
}