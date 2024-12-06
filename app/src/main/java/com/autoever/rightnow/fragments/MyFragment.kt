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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import android.app.AlertDialog
import android.widget.ImageView
import android.widget.LinearLayout


class MyFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my, container, false)

        pieChart = view.findViewById(R.id.pie_chart)
        barChart = view.findViewById(R.id.bar_chart)
        
        setupPieChart()
        setupBarChart()

        // 차트 컨테이너에 클릭 리스너 설정
        view.findViewById<LinearLayout>(R.id.pie_chart_container).setOnClickListener {
            showPieChartDialog()
        }

        return view
    }

    /**
     * 파이 차트 : 일별 수익 추이
     * */
    private fun setupPieChart() {
        pieChart.setUsePercentValues(true)

        // 데이터 항목 생성
        val entries = ArrayList<PieEntry>().apply {
            add(PieEntry(14f, "20대"))
            add(PieEntry(22f, "30대"))
            add(PieEntry(7f, "40대"))
            add(PieEntry(31f, "50대"))
            add(PieEntry(26f, "60대"))
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
            valueTextSize = 10f
        }

        // 차트 설정
        pieChart.apply {
            data = PieData(pieDataSet)
            description.isEnabled = false
            isRotationEnabled = false
            centerText = "진단 결과"
            setEntryLabelColor(Color.BLACK)
            setCenterTextSize(10f)
            setDrawEntryLabels(false)

            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }
    }

    /**
     * 바 차트 : 일별 수익 추이
     * */
    private fun setupBarChart() {
        // 데이터 생성
        val entries = ArrayList<BarEntry>().apply {
            add(BarEntry(0f, 50f))    // 50만원
            add(BarEntry(1f, 80f))   
            add(BarEntry(2f, 120f))
            val add = add(BarEntry(3f, 90f))
            add(BarEntry(4f, 70f))
            add(BarEntry(5f, 40f))
        }


        // 차트 색상 설정
        val colorsItems = ArrayList<Int>().apply {
            add(resources.getColor(R.color.color1, null))
            add(resources.getColor(R.color.color2, null))
            add(resources.getColor(R.color.color3, null))
            add(resources.getColor(R.color.color4, null))
            add(resources.getColor(R.color.color5, null))
            add(resources.getColor(R.color.color1, null))
        }

        val barDataSet = BarDataSet(entries, "시간대별 수익").apply {
            colors = colorsItems
            valueTextColor = Color.BLACK
            valueTextSize = 12f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()}"  // 숫자만 표시
                }
            }
        }

        val xAxis = barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            valueFormatter = IndexAxisValueFormatter(arrayOf("6", "12", "15", "18", "21", "24"))
            granularity = 1f
        }

        barChart.apply {
            data = BarData(barDataSet)
            description.isEnabled = false
            legend.isEnabled = true
            setFitBars(true)
            animateY(1000)
            legend.isEnabled = false  // 범례 비활성화

            // 왼쪽 Y축 설정
            axisLeft.apply {
                axisMinimum = 0f
                setDrawGridLines(true)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.toInt()}만원"
                    }
                }
            }

            // 오른쪽 Y축 비활성화
            axisRight.isEnabled = false
        }
    }

    /**
     * 팝업
     * */
    private fun showPieChartDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_chart, null)

        // 새로운 차트 생성 및 설정 복사
        val dialogPieChart = PieChart(requireContext())
        dialogPieChart.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // 원본 차트의 데이터와 설정을 복사
        dialogPieChart.data = pieChart.data
        dialogPieChart.description = pieChart.description
        dialogPieChart.isRotationEnabled = pieChart.isRotationEnabled
        dialogPieChart.centerText = pieChart.centerText
        dialogPieChart.setDrawEntryLabels(false)

        // 다이얼로그의 컨테이너에 새 차트 추가
        val chartContainer = dialogView.findViewById<LinearLayout>(R.id.chart_container)
        chartContainer.addView(dialogPieChart)

        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()

        // X 버튼 클릭 리스너
        dialogView.findViewById<ImageView>(R.id.btn_close).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}