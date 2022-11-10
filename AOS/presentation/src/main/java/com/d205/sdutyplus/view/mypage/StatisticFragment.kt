package com.d205.sdutyplus.view.mypage

import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentStatisticBinding
import com.d205.sdutyplus.uitls.showToast


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

}