package com.blood.utils

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.Editable
import android.text.TextPaint
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.viewpager.widget.ViewPager

object ViewUtils {
    fun TextView.setGradiantText(start: String, end: String) {
        val paint: TextPaint = this.paint
        val width: Float = paint.measureText(this.text.toString())

        val textShader: Shader = LinearGradient(
            0f, 0f, width, this.textSize, intArrayOf(
                Color.parseColor(start), Color.parseColor(end)
            ), null, Shader.TileMode.CLAMP
        )
        this.paint.shader = textShader
    }

    fun View.isVisible(): Boolean {
        return this.visibility == View.VISIBLE
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.isGone(): Boolean {
        return this.visibility == View.GONE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.isInvisible(): Boolean {
        return this.visibility == View.INVISIBLE
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }

    fun View.clickWithDebounce(debounceTime: Long = 500L, action: () -> Unit) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0
            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                else action()
                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }

    fun EditText.mustRemoveZeroFirst() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var text = this@mustRemoveZeroFirst.text.toString()
                if (text.isNotEmpty() && text.startsWith("0")) {
                    while (text.startsWith("0")) {
                        text = text.drop(1)
                    }
                    this@mustRemoveZeroFirst.setText(text)
                }
            }

            override fun afterTextChanged(editable: Editable?) {

            }
        })
    }

    fun EditText.textTrim() = this.text.toString().trim()

    fun EditText.intNumber() = try {
        Integer.parseInt(this.textTrim())
    } catch (ex: Exception) {
        0
    }

    fun Spinner.textSelected() = this.selectedItem.toString()

    fun Spinner.intNumber() = try {
        Integer.parseInt(this.textSelected())
    } catch (ex: Exception) {
        0
    }

    fun EditText.isBlank() = this.text.toString().trim().isBlank()

    fun TextView.textTrim() = this.text.toString().trim()

    fun ViewPager.autoScroll(interval: Long) {

        val handler = Handler(Looper.getMainLooper())
        var scrollPosition = 0

        val runnable = object : Runnable {

            override fun run() {
                try {
                    val count = adapter?.count ?: 0
                    setCurrentItem(scrollPosition++ % count, true)

                    handler.postDelayed(this, interval)
                } catch (_: java.lang.Exception) {

                }
            }
        }

        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                scrollPosition = position + 1
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Not necessary
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // Not necessary
            }
        })

        handler.post(runnable)
    }
}