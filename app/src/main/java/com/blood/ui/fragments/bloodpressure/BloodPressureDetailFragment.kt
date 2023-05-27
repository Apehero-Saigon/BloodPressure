package com.blood.ui.fragments.bloodpressure

import com.blood.base.BaseFragment
import com.blood.ui.fragments.home.HomeViewModel
import com.blood.utils.AdsUtils.BannerUtils.loadBanner
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentBloodPressureDetailBinding

class BloodPressureDetailFragment : BaseFragment<HomeViewModel, FragmentBloodPressureDetailBinding>(
    R.layout.fragment_blood_pressure_detail, HomeViewModel::class.java
) {
    override fun initAds() {
        super.initAds()

        binding.flBanner.loadBanner(
            requireActivity(),
            BuildConfig.banner_home,
            prefUtils.isShowBannerHome
        )
    }
}