package com.d205.sdutyplus.view.mypage

import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentStatisticBinding
import com.github.mikephil.charting.data.BarEntry


class StatisticFragment : BaseFragment<FragmentStatisticBinding>(R.layout.fragment_statistic) {

    override fun initOnViewCreated() {

        val pie = AnyChart.pie()

//        pie.setOnClickListener(object : ListenersInterface.OnClickListener(arrayOf("x", "value")) {
//            override fun onClick(event: Event?) {
//                requireContext().showToast(event!!.data["x"] + ":" + event.data["value"])
//            }
//        })
        val data = mutableListOf<DataEntry>()
        data.add(ValueDataEntry("Apples", 6371664))
        data.add(ValueDataEntry("Pears", 789622))
        data.add(ValueDataEntry("Bananas", 7216301))
        data.add(ValueDataEntry("Grapes", 1486621))
        data.add(ValueDataEntry("Oranges", 1200000))

        pie.data(data)
        pie.title("Fruits imported in 2015 (in kg)")

        //pie.labels().position("outside")

//        pie.legend().title().enabled(true)
//        pie.legend().title()
//            .text("Retail channels")
//            .padding(0.0, 0.0, 10.0, 0.0)
//
//        pie.legend()
//            .position("center-bottom")
//            .itemsLayout(LegendLayout.HORIZONTAL)
//            .align(Align.CENTER)

        binding.anyChartView.setChart(pie)
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