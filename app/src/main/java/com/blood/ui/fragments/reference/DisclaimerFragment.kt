package com.blood.ui.fragments.reference

import androidx.navigation.fragment.findNavController
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.utils.AdsUtils.BannerUtils.loadBanner
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentDisclaimerBinding

class DisclaimerFragment : BaseFragment<BaseViewModel, FragmentDisclaimerBinding>(
    R.layout.fragment_disclaimer,
    BaseViewModel::class.java
) {
    override fun initAds() {
        binding.flBanner.loadBanner(
            requireActivity(),
            BuildConfig.banner_home,
            prefUtils.isShowBannerHome
        )
    }

    override fun initListener() {
        super.initListener()
        binding.btnBack.clickWithDebounce {
            findNavController().navigateUp()
        }
    }
}