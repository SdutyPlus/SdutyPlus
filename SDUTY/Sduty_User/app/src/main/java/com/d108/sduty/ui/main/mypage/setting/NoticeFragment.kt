package com.d108.sduty.ui.main.mypage.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.d108.sduty.adapter.NoticeAdapter
import com.d108.sduty.databinding.FragmentNoticeBinding
import com.d108.sduty.ui.main.mypage.setting.viewmodel.SettingViewModel

private const val TAG = "NoticeFragment"
class NoticeFragment : Fragment() {
    private lateinit var binding: FragmentNoticeBinding
    private val viewModel: SettingViewModel by viewModels()
    private lateinit var noticeAdapter: NoticeAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noticeAdapter = NoticeAdapter()
        binding.apply {
            commonTopBack.setOnClickListener {
                findNavController().popBackStack()
            }
            recyclerNotice.apply {
                adapter = noticeAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }

        viewModel.apply {
            getNoticeList()
            noticeList.observe(viewLifecycleOwner){
                noticeAdapter.list = it
            }
        }
    }
}