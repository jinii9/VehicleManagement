package com.autoever.rightnow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.autoever.rightnow.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import android.graphics.Color
import com.github.mikephil.charting.components.Legend

class MyFragment : Fragment() {

    private lateinit var pieChart: PieChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my, container, false)

        pieChart = view.findViewById(R.id.pie_chart)
        setupPieChart()

        return view
    }

    private fun setupPieChart() {
        pieChart.setUsePercentValues(true)

        // 데이터 항목 생성
        val entries = ArrayList<PieEntry>().apply {
            add(PieEntry(14f, "20대"))
            add(PieEntry(22f, "30대"))
            add(PieEntry(7f, "40대"))
            add(PieEntry(31f, "50대"))
            add(PieEntry(26f, "60대 이상"))
        }

        // 차트 색상 설정
        val colorsItems = ArrayList<Int>().apply {
            add(resources.getColor(R.color.color1, null))
            add(resources.getColor(R.color.color2, null))
            add(resources.getColor(R.color.color3, null))
            add(resources.getColor(R.color.color4, null))
            add(resources.getColor(R.color.color5, null))
        }

        // 데이터셋 설정
        val pieDataSet = PieDataSet(entries, "").apply {
            colors = colorsItems
            valueTextColor = Color.BLACK
            valueTextSize = 18f
        }

//        // 차트 설정
//        pieChart.apply {
//            data = PieData(pieDataSet)
//            description.isEnabled = false
//            isRotationEnabled = false
//            centerText = "진단 결과"
//            setEntryLabelColor(Color.BLACK)
//            setCenterTextSize(20f)
//            animateY(1400, Easing.EaseInOutQuad)
//            animate()
//        }
        // 차트 설정
        pieChart.apply {
            data = PieData(pieDataSet)
            description.isEnabled = false
            isRotationEnabled = false
            centerText = "진단 결과"
            setEntryLabelColor(Color.BLACK)
            setCenterTextSize(20f)

            // 범례 설정
            legend.apply {
                isEnabled = true                   // 범례 활성화
                verticalAlignment = Legend.LegendVerticalAlignment.CENTER  // 수직 중앙 정렬
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT  // 오른쪽 정렬
                orientation = Legend.LegendOrientation.VERTICAL  // 수직 방향으로 표시
                setDrawInside(false)              // 차트 바깥에 표시
            }

            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }
    }
}