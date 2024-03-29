package com.d205.sdutyplus.view.report.graph

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentGraphBinding
import com.d205.sdutyplus.view.report.ReportViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class graphFragment : BaseFragment<FragmentGraphBinding>(R.layout.fragment_graph) {
    private val reportViewModel: ReportViewModel by activityViewModels()
    private lateinit var colorPalette: List<Int>
    private lateinit var graphList: List<Int>
    private val graphTitle: List<String> by lazy {
        listOf("2시간 미만", "2~4시간", "4~6시간", "6~8시간", "8시간 이상")
    }
    private var continuous: Int = -1
    private var studyTime: Int = -1


    override fun initOnViewCreated() {
        initView()
        initClickListener()
        initViewModelCallBack()
    }

    private fun initClickListener() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModelCallBack() {
        reportViewModel.continuous.observe(viewLifecycleOwner) {
            continuous = it
        }
        reportViewModel.studyTime.observe(viewLifecycleOwner) {
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

            pieDataSet.apply {
                colors = colorPalette
                sliceSpace = 3f
                selectionShift = 5f

//                setValueLinePart1OffsetPercentage(80.0f)
//                setValueLinePart1Length(0.3f)
//                setValueLinePart2Length(0.5f)
//                setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE)
            }

            val pieData = PieData(pieDataSet)

            pieData.apply {
                setValueFormatter(PercentFormatter())
                setValueTextSize(14f)
                setValueTextColor(Color.BLACK)
                setValueTypeface(Typeface.DEFAULT_BOLD)

            }

            binding.piechart.apply {

                setUsePercentValues(true)
                setExtraOffsets(5f, 10f, 5f, 5f)
                setDragDecelerationFrictionCoef(0.95f)

                setExtraOffsets(20f, 0f, 20f, 0f)
                setEntryLabelTextSize(16F)
                setEntryLabelColor(Color.BLACK)

                setEntryLabelTypeface(Typeface.DEFAULT)

                setDrawHoleEnabled(true)
                setHoleColor(Color.WHITE)

                setTransparentCircleColor(Color.WHITE)
                setTransparentCircleAlpha(110)

                setRotationAngle(0f)
                // enable rotation of the chart by touch
                setRotationEnabled(true)
                //setHighlightPerTapEnabled(true)



                // 부가 설명
                description.isEnabled = false

                isDrawHoleEnabled = true
                // setHoleColor(Color.WHITE)
                transparentCircleRadius = 61f
                animateY(1400, Easing.EaseInOutCubic)
                centerText = "회원님은\n${studyTime}시간 이상 \n공부하였습니다."
                setCenterTextSize(16F)
                data = pieData
                highlightValues(null)
                invalidate()
            }


            val l: Legend = binding.piechart.getLegend()
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.setDrawInside(false)
            l.isEnabled = false
        }
    }

    private fun initView() {
        reportViewModel.getGraph()
    }

    private fun data(): ArrayList<PieEntry> {
        val datavalue: ArrayList<PieEntry> = ArrayList()

        for(i in 0..4){
            if(graphList[i] != 0){
                datavalue.add(PieEntry(graphList[i].toFloat(), graphTitle[i]))
            }
        }


        return datavalue
    }



}
