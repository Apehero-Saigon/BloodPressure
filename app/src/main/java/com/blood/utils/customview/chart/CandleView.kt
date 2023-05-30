package com.blood.utils.customview.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.blood.utils.AppUtils.px
import kotlin.math.max

class CandleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var colorTop: Int = Color.parseColor("#000000")
        set(value) {
            paintTop.color = ContextCompat.getColor(context, value)
            paintDashTop.color = ContextCompat.getColor(context, value)
            field = value
            this.invalidate()
        }
    private var paintTop: Paint = Paint()
    private var paintDashTop = Paint()

    var colorBottom: Int = Color.parseColor("#000000")
        set(value) {
            paintBottom.color = ContextCompat.getColor(context, value)
            paintDashBottom.color = ContextCompat.getColor(context, value)
            field = value
            this.invalidate()
        }
    private var paintBottom: Paint = Paint()
    private var paintDashBottom = Paint()

    private var dashPathEffect: DashPathEffect? = null
    private var paintTextTop = Paint()
    private var paintTextBottom = Paint()

    var startDraw: Float = 0f
    var valueTop: Float = 30f
    var topPosY = 30.px
        get() = field + circleRadius + textSizeValue

    var valueBottom: Float = 30f
    var bottomPosY = 30.px
        get() = field + circleRadius + textSizeValue

    var circleRadius = 30.px
        set(value) {
            updateDashWidth()
            field = value
        }

    var textSizeValue = 16.px
        set(value) {
            paintTextTop.textSize = value
            paintTextBottom.textSize = value
            field = value
        }

    init {
        paintTop.style = Paint.Style.FILL
        paintTop.color = Color.WHITE
        paintTop.color = colorTop

        paintBottom.style = Paint.Style.FILL
        paintBottom.color = Color.WHITE
        paintBottom.color = colorBottom

        updateDashWidth()

        paintDashTop.style = Paint.Style.STROKE
        paintDashTop.color = colorTop

        paintDashBottom.style = Paint.Style.FILL_AND_STROKE
        paintDashBottom.color = colorBottom

        paintTextTop = TextPaint().apply {
            isAntiAlias = true
            // typeface = Typeface.createFromAsset(context.assets, logoFont)
            color = Color.parseColor("#000000")
            style = Paint.Style.FILL
            textSize = textSizeValue
        }

        paintTextBottom = TextPaint().apply {
            isAntiAlias = true
            // typeface = Typeface.createFromAsset(context.assets, logoFont)
            color = Color.parseColor("#000000")
            style = Paint.Style.FILL
            textSize = CandleChart.textVerticalAxisSize
        }
    }

    private fun updateDashWidth() {
        val dasWidth = circleRadius * 0.5f
        paintDashTop.strokeWidth = dasWidth
        paintDashBottom.strokeWidth = dasWidth
        dashPathEffect = DashPathEffect(floatArrayOf(dasWidth, dasWidth), 0f)
        paintDashTop.pathEffect = dashPathEffect
        paintDashBottom.pathEffect = dashPathEffect
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val x = max(width.toFloat(), 1f)
        val y = max(height.toFloat(), 1f)

        val cirPosX = this.topPosY
        val cirPosY = this.bottomPosY
        val centerY = (y - cirPosY + y - cirPosX) / 2
        canvas.drawLine(x / 2f, y - cirPosX + circleRadius, x / 2f, centerY, paintDashTop)
        canvas.drawLine(x / 2f, y - cirPosY - circleRadius, x / 2f, centerY, paintDashBottom)

        canvas.drawCircle(x / 2f, y - cirPosX, circleRadius, paintTop)
        canvas.drawCircle(x / 2f, y - cirPosY, circleRadius, paintBottom)

        val topText = valueTop.toString().replace(".0", "")
        val baselineTop: Float = -paintTextTop.ascent()
        val widthTextTop = paintTextTop.measureText(topText)
        val heightTextTop = (baselineTop + paintTextTop.descent())
        drawText(
            canvas,
            topText,
            x / 2 - widthTextTop / 2f,
            y - cirPosX - circleRadius - heightTextTop / 2,
            paintTextTop
        )

        val bottomText = valueBottom.toString().replace(".0", "")
        val baselineBottom: Float = -paintTextBottom.ascent()
        val widthTextBottom = paintTextBottom.measureText(bottomText)
        val heightTextBottom = (baselineBottom + paintTextBottom.descent())
        drawText(
            canvas,
            bottomText,
            x / 2 - widthTextBottom / 2f,
            y - cirPosY + circleRadius + heightTextBottom,
            paintTextBottom
        )
    }

    private fun drawText(canvas: Canvas, text: String, x: Float, y: Float, paint: Paint) {
//        val baseline: Float = -paintTimeText.ascent()
//        val widthText = paintTimeText.measureText(text)
//        val heightText = (baseline + paintTimeText.descent())

        canvas.drawText(text, x, y, paint)
    }
}