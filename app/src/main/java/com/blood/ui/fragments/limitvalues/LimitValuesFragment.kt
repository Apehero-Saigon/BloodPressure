package com.blood.ui.fragments.limitvalues

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.common.Constant
import com.blood.common.enumdata.FilterType
import com.blood.data.LimitValue
import com.blood.ui.adapters.LimitValueAdapter
import com.blood.ui.fragments.insight.InsightBloodPressureFragment
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.customview.HeaderView
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BR
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentLimitValuesBinding

class LimitValuesFragment : BaseFragment<BaseViewModel, FragmentLimitValuesBinding>(
    R.layout.fragment_limit_values, BaseViewModel::class.java
), HeaderView.Listener {

    val adapter = LimitValueAdapter()

    override fun init(inflater: LayoutInflater, container: ViewGroup) {
        super.init(inflater, container)
        binding.setVariable(BR.limitValuesFragment, this)
    }

    override fun initAds() {

    }

    override fun initView() {
        super.initView()
        binding.rcvLimitValue.adapter = adapter
    }

    override fun initData() {
        updateTypeValue(prefUtils.typeLimitValue)
    }

    override fun initListener() {
        with(binding) {
            tv2017ACC.clickWithDebounce {
                if (!tv2017ACC.isSelected) {
                    updateTypeValue(Constant.ACC_AHA_2017)
                }
            }

            tv2018ESC.clickWithDebounce {
                if (!tv2018ESC.isSelected) {
                    updateTypeValue(Constant.ESC_ESH_2018)
                }
            }
        }
    }

    private fun updateTypeValue(type: String) {
        with(binding) {
            if (type == Constant.ACC_AHA_2017) {
                adapter.updateData(LimitValue.getList2017ACCAHA())
            } else {
                adapter.updateData(LimitValue.getList2018ESCESH())
            }
            tv2017ACC.isSelected = type == Constant.ACC_AHA_2017
            tv2017ACC.setTextColor(
                ContextCompat.getColor(
                    requireContext(), if (tv2017ACC.isSelected) R.color.white else R.color.black
                )
            )

            tv2018ESC.isSelected = type == Constant.ESC_ESH_2018
            tv2018ESC.setTextColor(
                ContextCompat.getColor(
                    requireContext(), if (tv2018ESC.isSelected) R.color.white else R.color.black
                )
            )
        }
        prefUtils.typeLimitValue = type
    }

    override fun onHeaderBackPressed() {
        findNavController().navigateUp()
    }
}