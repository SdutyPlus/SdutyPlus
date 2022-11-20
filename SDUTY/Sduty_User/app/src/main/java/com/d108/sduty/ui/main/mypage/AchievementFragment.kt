package com.d108.sduty.ui.main.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.d108.sduty.adapter.AchievementAdapter
import com.d108.sduty.databinding.FragmentAchievementBinding
import com.d108.sduty.model.dto.Achievement
import com.d108.sduty.ui.main.mypage.dialog.DialogAchivement

// 업적 페이지 - 업적 내용, 달성한 업적, 달성 가능한 업적, 숨겨진 업적 표시
private const val TAG ="AchievementFragment"
class AchievementFragment : Fragment() {
    private lateinit var binding: FragmentAchievementBinding
    private lateinit var achievementAdapter: AchievementAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAchievementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()

    }

    private fun initView() {
        achievementAdapter = AchievementAdapter(requireActivity())
        achievementAdapter.onClickListener = object : AchievementAdapter.OnClickListener{
            override fun onClick(view: View, position: Int) {
                DialogAchivement(requireContext()).let {
                    it.show(parentFragmentManager.beginTransaction(), null)
                }
            }
        }
        binding.apply {
            lifecycleOwner = this@AchievementFragment
            recyclerAchievement.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = achievementAdapter
            }
        }
    }

    private fun initViewModel() {
        val list = mutableListOf<Achievement>()
        for(i in 0.. 25){
            list.add(Achievement(i, "업적 ${i}","업적", 0))
        }
        achievementAdapter.list = list
    }

}
