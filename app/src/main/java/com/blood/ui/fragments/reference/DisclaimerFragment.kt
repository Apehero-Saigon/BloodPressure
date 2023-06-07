package com.blood.ui.fragments.reference

import android.view.LayoutInflater
import android.view.ViewGroup
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.utils.AdsUtils.BannerUtils.Companion.loadBanner
import com.blood.utils.customview.HeaderView
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BR
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentDisclaimerBinding

class DisclaimerFragment : BaseFragment<BaseViewModel, FragmentDisclaimerBinding>(
    R.layout.fragment_disclaimer, BaseViewModel::class.java
), HeaderView.Listener {

    override fun init(inflater: LayoutInflater, container: ViewGroup) {
        super.init(inflater, container)
        binding.setVariable(BR.disclaimerFragment, this)
    }

    override fun initAds() {
        binding.flBanner.loadBanner(
            requireActivity(), BuildConfig.banner_home, prefUtils.isShowBannerHome
        )
    }

    override fun onHeaderBackPressed() {
        safeBackNav()
    }
}