package com.blood.ui.fragments.measurementguideline

import com.blood.App
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.common.Constant
import com.blood.utils.AdsUtils
import com.blood.utils.FirebaseUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.ViewUtils.gone
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentMeasurementDefaultBinding

class MeasurementGuidelineDefaultFragment :
    BaseFragment<BaseViewModel, FragmentMeasurementDefaultBinding>(
        R.layout.fragment_measurement_default, BaseViewModel::class.java
    ) {

    override fun backPressedWithExitPopup() = true

    override fun initAds() {
        adsUtils.nativeDefaultValue.showAds(requireActivity(), binding.flAds, reloadAfterShow = true)
    }

    override fun initData() {
        super.initData()
        FirebaseUtils.eventDisplayGuidelineScreen()
        updateTypeSelected(false)
        with(binding) {

            cl2018.clickWithDebounce {
                updateTypeSelected(false)
            }

            cl2017.clickWithDebounce {
                updateTypeSelected(true)
            }

            tvSave.clickWithDebounce {
                FirebaseUtils.eventClickGuidelineChoose()
                prefUtils.typeLimitValue =
                    if (cl2017.isSelected) Constant.ACC_AHA_2017 else Constant.ESC_ESH_2018
                val action =
                    MeasurementGuidelineDefaultFragmentDirections.actionMeasurementGuidelineDefaultFragmentToHomeFragment()
                safeNav(action)
            }
        }
    }

    private fun updateTypeSelected(is2017Value: Boolean) {
        binding.cl2017.isSelected = is2017Value
        binding.cl2018.isSelected = !is2017Value
    }
}