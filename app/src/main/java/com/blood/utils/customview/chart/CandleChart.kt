package com.blood.utils.customview.chart

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blood.common.Constant
import com.blood.data.BloodPressure
import com.blood.utils.AppUtils.px
import com.blood.utils.DateUtils.strDateTime
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.ChartCandleBinding
import java.util.*

class CandleChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    companion object {
        var PADDING_BOTTOM = 40.px
        var textVerticalAxisSize = 16.px
        var textValueSize = 10.px
        var pointChartSize = 12.px
        var blackLineHeight = 2.px

        var stepPX = 0f
        var stepValue = 0
        var startDrawText = 0f
        var heightText = 0f
        var maxValue = 0f
        var minValue = 0f

        var firstStep = 0
    }

    private var binding: ChartCandleBinding

    private var listData: List<Data> = mutableListOf()

    private var paintVerticalAxis = Paint()

    private var paintLineBottom = Paint()
    private var adapter = ChartDataAdapter(listData)

    init {
        setWillNotDraw(false)
        binding = ChartCandleBinding.inflate(LayoutInflater.from(context), this, true)
        initView()
        initPaint()
    }

    private fun initView() {
        binding.rcvChartDate.setPadding(PADDING_BOTTOM.toInt(), 0, 0, 0)
        binding.rcvChartDate.adapter = adapter
    }

    private fun initPaint() {
        paintVerticalAxis = TextPaint().apply {
            isAntiAlias = true
            // typeface = Typeface.createFromAsset(context.assets, logoFont)
            color = Color.BLACK
            style = Paint.Style.FILL
            textSize = textVerticalAxisSize
        }

        paintLineBottom = Paint().apply {
            isAntiAlias = true
            // typeface = Typeface.createFromAsset(context.assets, logoFont)
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = blackLineHeight
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val with = width.toFloat()
        val height = height.toFloat()

        drawVerticalAxis(canvas)

        canvas.drawLine(
            PADDING_BOTTOM, height - PADDING_BOTTOM, with, height - PADDING_BOTTOM, paintLineBottom
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun drawVerticalAxis(canvas: Canvas) {
        stepPX = (height - PADDING_BOTTOM) / 5f
        stepValue = calculatorStep(maxValue, minValue)
        if (stepPX != 0f && stepValue != 0) {
            adapter.notifyDataSetChanged()
        }

        var nextStep: Int = stepValue

        val baseline: Float = -paintVerticalAxis.ascent()
        heightText = (baseline + paintVerticalAxis.descent())

        while (nextStep < minValue) {
            nextStep += stepValue
            if (nextStep > minValue) {
                nextStep -= stepValue
                break
            }
        }
        startDrawText = height - PADDING_BOTTOM - heightText

        val firstPosY = startDrawText

        firstStep = nextStep
        canvas.drawText(nextStep.toString().replace(".0", ""), 10.px, firstPosY, paintVerticalAxis)

        val secondPosY = firstPosY - stepPX
        nextStep += stepValue
        canvas.drawText(nextStep.toString().replace(".0", ""), 10.px, secondPosY, paintVerticalAxis)

        nextStep += stepValue
        val thirdPosY = secondPosY - stepPX
        canvas.drawText(nextStep.toString().replace(".0", ""), 10.px, thirdPosY, paintVerticalAxis)

        nextStep += stepValue
        val fourPosY = thirdPosY - stepPX
        canvas.drawText(nextStep.toString().replace(".0", ""), 10.px, fourPosY, paintVerticalAxis)
    }

    fun setData(list: List<Data> = mutableListOf(), max: Float, min: Float) {
        this.listData = list

        var dataStr = ""
        for (data in listData) {
            val currentDateStr = data.date.strDateTime(Constant.FORMAT_DM)
            if (dataStr != currentDateStr) {
                data.isFirstDate = true
                dataStr = currentDateStr
            }
        }
        maxValue = max
        minValue = min
        adapter.setData(list)
        this.invalidate()
    }

    private fun calculatorStep(max: Float, min: Float): Int {
        val rawStep = (max - min) / 3
        return if (rawStep >= 45) {
            50
        } else if (rawStep >= 35) {
            40
        } else if (rawStep >= 25) {
            30
        } else if (rawStep >= 15) {
            20
        } else if (rawStep >= 10) {
            15
        } else if (rawStep >= 5) {
            10
        } else if (rawStep >= 2) {
            4
        } else if (rawStep >= 1) {
            2
        } else {
            1
        }
    }

    data class Data(
        var max: Float, val min: Float, var date: Date, var isFirstDate: Boolean = false
    )

    class ChartDataAdapter(var list: List<Data>) :
        RecyclerView.Adapter<ChartDataAdapter.ChartViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chart_max_min, parent, false)
            return ChartViewHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        @SuppressLint("NotifyDataSetChanged")
        fun setData(list: List<Data>) {
            this.list = list
            this.notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
            val data = list[holder.absoluteAdapterPosition]

            holder.standChart.colorTop = BloodPressure.getColorSystolic(data.max.toInt())
            holder.standChart.colorBottom = BloodPressure.getColorDiastolic(data.min.toInt())

            holder.standChart.circleRadius = pointChartSize.toInt() / 2f
            holder.standChart.textSizeValue = textValueSize
            holder.standChart.valueTop = data.max
            holder.standChart.valueBottom = data.min
            holder.standChart.startDraw = startDrawText

            val pxOneValue = stepPX / stepValue

            var posBottomY = holder.standChart.height - startDrawText - heightText / 2f
            posBottomY += pxOneValue * (data.min - firstStep)

            var posTopY = holder.standChart.height - startDrawText - heightText / 2f
            posTopY += pxOneValue * (data.max - firstStep)

            holder.standChart.topPosY = posTopY
            holder.standChart.bottomPosY = posBottomY

            holder.tvDate.visibility = if (data.isFirstDate) View.VISIBLE else View.INVISIBLE
            holder.tvDate.text = data.date.strDateTime(Constant.FORMAT_DM)
        }

        inner class ChartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val standChart: CandleView = view.findViewById(R.id.standChart)
            val tvDate: TextView = view.findViewById(R.id.tvDate)
        }
    }
}