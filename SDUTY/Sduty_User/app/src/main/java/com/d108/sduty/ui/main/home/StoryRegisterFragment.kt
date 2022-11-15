package com.d108.sduty.ui.main.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.d108.sduty.R
import com.d108.sduty.adapter.SelectedTagAdapter
import com.d108.sduty.common.ALL_TAG
import com.d108.sduty.common.ApplicationClass
import com.d108.sduty.common.NOT_PROFILE
import com.d108.sduty.databinding.FragmentStoryRegisterBinding
import com.d108.sduty.model.dto.InterestHashtag
import com.d108.sduty.model.dto.JobHashtag
import com.d108.sduty.model.dto.Story
import com.d108.sduty.ui.main.home.viewmodel.HomeViewModel
import com.d108.sduty.ui.sign.dialog.TagSelectDialog
import com.d108.sduty.ui.sign.viewmodel.TagViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.ui.viewmodel.StoryViewModel
import com.d108.sduty.utils.navigateBack
import com.d108.sduty.utils.safeNavigate
import com.d108.sduty.utils.showAlertDialog
import com.d108.sduty.utils.showToast

//게시물 등록 - 글 내용입력, 이미지 추가/ 미리보기, 카메라 or 이미지 선택, 태그 선택
private const val TAG ="StoryRegisterFragment"
class StoryRegisterFragment : Fragment(), PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: FragmentStoryRegisterBinding
    private val viewModel: HomeViewModel by viewModels()
    private val storyViewModel: StoryViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private val tagViewModel: TagViewModel by viewModels()
    // (공개 범위) 0 : 전체 공개, 1 : 팔로워만, 2 : 나만 보기
    private var disclosure = 0

    private var selectedTagList = mutableListOf<String>()
    private var interestHashtagList: MutableList<Int>? = null
    private val tagAdapter = SelectedTagAdapter(ALL_TAG)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoryRegisterBinding.inflate(inflater, container, false)
        binding.apply {
            etWrite.addTextChangedListener(object : TextWatcher {
                // 입력이 끝날 때 동작
                override fun afterTextChanged(p0: Editable?) {}
                // 입력하기 전에 동작
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                // 타이핑되는 텍스트에 변화가 있으면 동작
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    tvWordLength.text = "${etWrite.length()} / 200"
                    if (etWrite.length() > 200) {
                        //requireContext().showToast("최대 200자까지 입력 가능합니다.")
                        etWrite.setTextColor(Color.RED)
                        tvWordLength.setTextColor(Color.RED)
                    }
                    else {
                        etWrite.setTextColor(Color.BLACK)
                        tvWordLength.setTextColor(Color.BLACK)
                    }
                }
            })
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel.displayBottomNav(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()

    }

    private fun initViewModel() {
        tagViewModel.apply {
            getJobListValue()
        }
    }

    private fun initView(){
        if(ApplicationClass.storyBitmap != null){
            viewModel.setStoryImage(ApplicationClass.storyBitmap)
            ApplicationClass.storyBitmap = null
        }

        binding.apply {
            vm = viewModel
            lifecycleOwner = this@StoryRegisterFragment
            // 공개 범위 설정 버튼 클릭 시, 팝업 메뉴 보이기
            btnDisclosure.setOnClickListener {
                PopupMenu(requireContext(), it).apply {
                    // implements OnMenuItemClickListener
                    setOnMenuItemClickListener(this@StoryRegisterFragment)
                    inflate(R.menu.disclosure_menu)
                    show()
                }
            }
            btnAddImg.setOnClickListener {
                findNavController().safeNavigate(StoryRegisterFragmentDirections.actionStoryRegisterFragmentToStoryDecoFragment())
            }
            ivBack.setOnClickListener {
                navigateBack(requireActivity())
            }
            ivRegisterStory.setOnClickListener {
                // 게시물 정보 등록
                // 등록할 때, 초기 화면으로 visibility 다시 세팅...
                if(viewModel.bitmap.value == null){
                    requireContext().showToast("사진을 등록해 주세요")
                }else if(etWrite.text.isEmpty()) {
                    requireContext().showToast("내용을 입력해 주세요")
                }else{
                    storyViewModel.insertStory(Story(mainViewModel.user.value!!.seq, "", tagViewModel.jobTagMap.value!![mainViewModel.profile.value!!.job],  etWrite.text.toString(), disclosure, interestHashtagList), viewModel.bitmap.value!!)
                    requireContext().showToast("스토리가 등록 되었습니다")
                    navigateBack(requireActivity())
                }
            }
            selectedTagList.clear()
            selectedTagList.add(mainViewModel.profile.value!!.job)
            binding.recyclerSelectedTag.apply {
                adapter = tagAdapter
                tagAdapter.selectList = selectedTagList
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
            btnAddSubject.setOnClickListener {
                TagSelectDialog(requireContext()).let {
                    it.arguments = bundleOf("flag" to NOT_PROFILE)
                    it.show(parentFragmentManager, null)
                    it.onClickConfirm = object : TagSelectDialog.OnClickConfirm{
                        override fun onClick(selectedJob: JobHashtag?, selectedInterestList: MutableList<InterestHashtag>) {
                            if(selectedInterestList.size > 0){
                                selectedTagList.clear()
                                selectedTagList.add(mainViewModel.profile.value!!.job)
                                interestHashtagList = mutableListOf()
                                for(i in 0 until selectedInterestList.size){
                                    selectedTagList.add(selectedInterestList[i].name)
                                    interestHashtagList!!.add(selectedInterestList[i].seq)
                                }
                                tagAdapter.selectList = selectedTagList
                            }

                        }
                    }
                }
            }

        }
    }

    // 팝업 메뉴 클릭 이벤트 설정
    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.privateDisclosure -> {
                disclosure = 0
                binding.btnDisclosure.text = "나만 보기"
//                requireContext().showToast("나만 보기 클릭 : " + disclosure)
                true
            }
            R.id.followerDisclosure -> {
                disclosure = 1
                binding.btnDisclosure.text = "팔로워 공개"
//                requireContext().showToast("팔로워만 공개 클릭 : " + disclosure)
                true
            }
            R.id.publicDisclosure -> {
                disclosure = 2
                binding.btnDisclosure.text = "전체 공개"
//                requireContext().showToast("전체 공개 클릭 : " + disclosure)
                true
            }
            else -> false
        }
    }
}
