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
import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import android.app.AlertDialog
import android.widget.ImageView
import android.widget.LinearLayout
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MyFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart
    private lateinit var lineChart: LineChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my, container, false)

        pieChart = view.findViewById(R.id.pie_chart)
        barChart = view.findViewById(R.id.bar_chart)
        lineChart = view.findViewById(R.id.line_chart)
        
        setupPieChart()
        setupBarChart()
        setupLineChart()

        // 차트 컨테이너에 클릭 리스너 설정
        view.findViewById<LinearLayout>(R.id.pie_chart_container).setOnClickListener {
            showPieChartDialog()
        }
        view.findViewById<LinearLayout>(R.id.bar_chart_container).setOnClickListener {
            showBarChartDialog()
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
     * 라인 차트 : 일별 수익 추이
     * */
    private fun setupLineChart() {
        // 데이터 생성
        val entries = ArrayList<Entry>().apply {
            add(Entry(0f, 30f))  // 1일
            add(Entry(1f, 45f))  // 2일
            add(Entry(2f, 35f))  // 3일
            add(Entry(3f, 60f))  // 4일
            add(Entry(4f, 50f))  // 5일
            add(Entry(5f, 70f))  // 6일
            add(Entry(7f, 45f))  // 7일
            add(Entry(8f, 45f))  // 7일
            add(Entry(9f, 45f))  // 7일
            add(Entry(10f, 45f))  // 7일
            add(Entry(11f, 45f))  // 7일
            add(Entry(12f, 45f))  // 7일
            add(Entry(13f, 45f))  // 7일
        }

        val lineDataSet = LineDataSet(entries, "일별 수익").apply {
            color = resources.getColor(R.color.color1, null)
            valueTextColor = Color.BLACK
            valueTextSize = 12f
            setDrawFilled(true)
            fillColor = resources.getColor(R.color.color1, null)
            fillAlpha = 30
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()}"
                }
            }
        }

        lineChart.apply {
            data = LineData(lineDataSet)
            description.isEnabled = false
            legend.isEnabled = false

            // X축 설정
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${(value + 1).toInt()}일"
                    }
                }
                granularity = 1f
            }

            // Y축 설정
            axisLeft.apply {
                axisMinimum = 0f
                setDrawGridLines(true)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.toInt()}만원"
                    }
                }
            }
            axisRight.isEnabled = false

            animateX(1000)
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

    private fun showBarChartDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_chart, null)

        // 새로운 차트 생성 및 설정 복사
        val dialogBarChart = BarChart(requireContext())
        dialogBarChart.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // 원본 차트의 데이터와 설정을 복사
        dialogBarChart.data = barChart.data
        dialogBarChart.description = barChart.description
        dialogBarChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            valueFormatter = IndexAxisValueFormatter(arrayOf("6", "12", "15", "18", "21", "24"))
            granularity = 1f
        }

        // Y축 설정 복사
        dialogBarChart.axisLeft.apply {
            axisMinimum = 0f
            setDrawGridLines(true)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()}만원"
                }
            }
        }
        dialogBarChart.axisRight.isEnabled = false

        // 다이얼로그의 컨테이너에 새 차트 추가
        val chartContainer = dialogView.findViewById<LinearLayout>(R.id.chart_container)
        chartContainer.addView(dialogBarChart)

        dialogBuilder.setView(dialogView)
        val dialog = dialogBuilder.create()

        // X 버튼 클릭 리스너
        dialogView.findViewById<ImageView>(R.id.btn_close).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}