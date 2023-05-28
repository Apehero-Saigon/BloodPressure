package com.blood.ui.fragments.insight

import androidx.navigation.fragment.findNavController
import com.blood.base.BaseFragment
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.ViewUtils.gone
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentInsightBloodPressureBinding

class InsightBloodPressureFragment :
    BaseFragment<InsightViewModel, FragmentInsightBloodPressureBinding>(
        R.layout.fragment_insight_blood_pressure, InsightViewModel::class.java
    ) {

    override fun initAds() {
        if (isNetworkConnected() && prefUtils.isShowNativeRecentAction) {
            adsUtils.nativeRecentAction.showAds(
                requireActivity(),
                BuildConfig.native_recent_action,
                null,
                R.layout.layout_native_medium_custom,
                binding.flAds,
                false,
                reloadAfterShow = true
            )
        } else {
            binding.flAds.gone()
        }
    }

    override fun initData() {
        viewModel.loadListBlood()
    }

    override fun initListener() {
        super.initListener()

        with(binding) {

            btnBack.clickWithDebounce {
                findNavController().navigateUp()
            }

            btnMeasureNow.clickWithDebounce {
                val action =
                    InsightBloodPressureFragmentDirections.actionInsightBloodPressureFragmentToBloodPressureEditFragment()
                action.modeAdd = true
                action.mustShowBackButton = true
                findNavController().navigate(action)
            }
        }
    }
}