package com.d205.sdutyplus.view.report.graph

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentGraphBinding
import com.d205.sdutyplus.view.report.ReportViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class graphFragment : BaseFragment<FragmentGraphBinding>(R.layout.fragment_graph) {
    private val reportViewModel: ReportViewModel by activityViewModels()
    private lateinit var colorPalette: List<Int>
    private lateinit var graphList: List<Int>
    private var continuous: Int = -1
    private var studyTime: Int = -1


    override fun initOnViewCreated() {
        initView()
        initViewModelCallBack()
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModelCallBack() {
        reportViewModel.continuous.observe(viewLifecycleOwner) {
            Log.d("TAG", "initViewModelCallBack1: ${it}")
            continuous = it
        }
        reportViewModel.studyTime.observe(viewLifecycleOwner) {
            Log.d("TAG", "initViewModelCallBack2: ${it}")
            studyTime = it
        }

        reportViewModel.dailyTime.observe(viewLifecycleOwner) {
            binding.tvTitle.text = "${continuous}일 연속으로 공부하였습니다."
            graphList = it

            colorPalette = listOf(
                ContextCompat.getColor(requireContext(), R.color.sduty_graph_sky_blue),
                ContextCompat.getColor(requireContext(), R.color.sduty_graph_blue),
                ContextCompat.getColor(requireContext(), R.color.sduty_graph_mix_violet),
                ContextCompat.getColor(requireContext(), R.color.sduty_graph_purple),
                ContextCompat.getColor(requireContext(), R.color.sduty_graph_pink)
            )

            val pieDataSet = PieDataSet(data(), "공부시간")

            val pieData = PieData(pieDataSet)


            pieDataSet.apply {
                colors = colorPalette
                sliceSpace = 3f
                selectionShift = 5f
            }

            pieData.apply {
                setValueTextSize(16F)
                setValueTextColor(Color.BLACK)
                val des = Description()
                des.text = "시간별 공부시간"
            }

            binding.piechart.apply {
                setUsePercentValues(true)
                // 부가 설명
                description.isEnabled = false
                setExtraOffsets(0f, 5f, 0f, 5f)
                isDrawHoleEnabled = true
                setHoleColor(Color.WHITE)
                transparentCircleRadius = 61f
                animateY(1000, Easing.EaseInOutCubic)
                centerText = "${studyTime}시간 이상 \n공부하였습니다."
                setCenterTextSize(20F)
                data = pieData
                invalidate()
            }
        }
    }

    private fun initView() {
        reportViewModel.getGraph()
    }

    private fun data(): ArrayList<PieEntry> {
        val datavalue: ArrayList<PieEntry> = ArrayList()

        datavalue.add(PieEntry(graphList[0].toFloat(), "0~2시간"))
        datavalue.add(PieEntry(graphList[1].toFloat(), "2~4시간"))
        datavalue.add(PieEntry(graphList[2].toFloat(), "4~6시간"))
        datavalue.add(PieEntry(graphList[3].toFloat(), "6~8시간"))
        datavalue.add(PieEntry(graphList[4].toFloat(), "8시간 이상"))

        return datavalue
    }


}
