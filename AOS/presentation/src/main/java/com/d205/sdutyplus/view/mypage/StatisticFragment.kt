package com.d205.sdutyplus.view.mypage

import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentStatisticBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.data.BarEntry



class StatisticFragment : BaseFragment<FragmentStatisticBinding>(R.layout.fragment_statistic),
    OnChartValueSelectedListener {

    override fun initOnViewCreated() {
        binding.chart1.apply {
            setOnChartValueSelectedListener(this@StatisticFragment)
            description.isEnabled = false
            setPinchZoom(false)
            setDrawBarShadow(false)
            //setDrawGridBackground(false)
            val mv = MarkerView(this@StatisticFragment.requireContext(), R.layout.view_bar_marker)
            mv.chartView = this
            marker = mv
        }
        val l = binding.chart1.legend
        l.apply {
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            orientation = Legend.LegendOrientation.VERTICAL
            setDrawInside(true)
            //l.typeface = tfLight
            l.yOffset = 0f
            l.xOffset = 10f
            l.yEntrySpace = 0f
            l.textSize = 8f
        }
        val xAxis = binding.chart1.xAxis
        xAxis.apply {
            //typeface = tfLight
            granularity = 1f
            setCenterAxisLabels(true)
            valueFormatter = object : ValueFormatter() {
                //                override fun getFormattedValue(value: Float, axis: AxisBase?): String {
//                    return value.toString()
//                }
                override fun getFormattedValue(value: Float): String {
                    return value.toString()
                }

            }
        }

        val leftAxis = binding.chart1.axisLeft
        leftAxis.apply {
            //typeface = tfLight
            valueFormatter = LargeValueFormatter()
            setDrawGridLines(false)
            spaceTop = 35f
            axisMinimum = 0f // this replaces setStartAtZero(true)

        }

        binding.chart1.axisRight.isEnabled = false
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        //TODO("Not yet implemented")
    }

    override fun onNothingSelected() {
        //TODO("Not yet implemented")
    }

    fun setting() {
        val groupSpace = 0.08f
        val barSpace = 0.03f // x4 DataSet

        val barWidth = 0.2f
        val startYear = 1980
        val endYear = startYear + 10

        val values1 = mutableListOf<BarEntry>()
        val values2 = mutableListOf<BarEntry>()

        for (i in startYear until endYear) {
            values1.add(BarEntry(i.toFloat(), (Math.random() * 10).toFloat()))
            values2.add(BarEntry(i.toFloat(), (Math.random() * 10).toFloat()))
        }

    }

}