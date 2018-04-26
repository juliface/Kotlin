package com.lghdb.healthy

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val entries_mon = mutableListOf<Entry>()
        entries_mon.add(Entry(1f, 78f))
        entries_mon.add(Entry(2f, 75.4f))
        entries_mon.add(Entry(3f, 74f))
        entries_mon.add(Entry(4f, 75.6f))
        entries_mon.add(Entry(5f, 77.6f))


        val entries_aft = mutableListOf<Entry>()
        entries_aft.add(Entry(1f, 79f))
        entries_aft.add(Entry(2f, 76.4f))
        entries_aft.add(Entry(3f, 70f))
        entries_aft.add(Entry(4f, 69.6f))
        entries_aft.add(Entry(5f, 73.6f))

        val dataSet_mon = LineDataSet(entries_mon, "3月体重变化情况(上午)")
        dataSet_mon.colors = ColorTemplate.VORDIPLOM_COLORS.asList()
        dataSet_mon.valueTextColor = Color.BLACK
        dataSet_mon.isHighlightEnabled = true
        dataSet_mon.highLightColor = Color.BLUE

        val dataSet_aft = LineDataSet(entries_aft, "3月体重变化情况(下午)")
        dataSet_aft.colors = ColorTemplate.PASTEL_COLORS.asList()
        dataSet_aft.valueTextColor = Color.BLACK
        dataSet_aft.isHighlightEnabled = true
        dataSet_aft.highLightColor = Color.BLUE

        weightLine.data = LineData(listOf(dataSet_mon, dataSet_aft))

        val axisLeft = weightLine.axisLeft
        axisLeft.addLimitLine(LimitLine(140f,"体重").apply {
            lineWidth = 8f
            textColor = Color.GRAY
            textSize =14f
        })
        weightLine.xAxis.position = XAxis.XAxisPosition.BOTTOM
        weightLine.xAxis.granularity = 1f
        weightLine.description = Description().apply { text="体重变化" }
        weightLine.xAxis.setValueFormatter { value, axis ->
            "${value.toInt().toString()}月"
        }
        weightLine.invalidate()

        start.setOnClickListener{
            for (i in 1..100){

            }

        }

    }


}
