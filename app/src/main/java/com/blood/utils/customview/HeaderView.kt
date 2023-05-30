package com.blood.utils.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.LayoutHeaderViewBinding

class HeaderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    var binding: LayoutHeaderViewBinding

    private var isShowBack = false
    private var isShowOption = false
    private var titleRes = 0
    private var optionRes = 0

    var listener: Listener? = null

    init {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.layout_header_view, null, false
        )
        addView(binding.root)

        context.theme.obtainStyledAttributes(attrs, R.styleable.HeaderView, 0, 0).run {
            isShowBack = getBoolean(R.styleable.HeaderView_showBackButton, false)
            isShowOption = getBoolean(R.styleable.HeaderView_showOptionButton, false)
            titleRes = getResourceId(R.styleable.HeaderView_headerTitle, 0)
            optionRes = getResourceId(R.styleable.HeaderView_optionIcon, 0)
        }

        with(binding) {
            btnBack.visibility = if (isShowBack) View.VISIBLE else View.GONE
            btnOption.visibility = if (isShowOption) View.VISIBLE else View.GONE

            if (titleRes != 0) {
                tvHeader.setText(titleRes)
            }

            if (titleRes != 0) {
                btnOption.setImageResource(optionRes)
            }

            btnBack.clickWithDebounce {
                listener?.onHeaderBackPressed()
            }

            btnOption.clickWithDebounce {
                listener?.onOptionPressed(btnOption)
            }
        }
    }

    interface Listener {
        fun onHeaderBackPressed() {

        }

        fun onOptionPressed(view: View) {

        }
    }
}