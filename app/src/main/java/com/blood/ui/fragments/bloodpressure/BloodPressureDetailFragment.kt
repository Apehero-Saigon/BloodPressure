package com.blood.ui.fragments.bloodpressure

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.blood.base.BaseFragment
import com.blood.utils.AdsUtils.BannerUtils.loadBanner
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentBloodPressureDetailBinding

class BloodPressureDetailFragment :
    BaseFragment<BloodPressureViewModel, FragmentBloodPressureDetailBinding>(
        R.layout.fragment_blood_pressure_detail, BloodPressureViewModel::class.java
    ) {

    val args: BloodPressureDetailFragmentArgs by navArgs()

    override fun initAds() {
        super.initAds()
        binding.flBanner.loadBanner(
            requireActivity(), BuildConfig.banner_home, prefUtils.isShowBannerHome
        )
    }

    override fun initData() {
        viewModel.getBloodPressureByID(args.id)
    }

    override fun initListener() {
        super.initListener()

        with(binding) {
            btnBack.clickWithDebounce {
                findNavController().navigateUp()
            }

            tvDisclaimer.clickWithDebounce {
                findNavController().navigate(BloodPressureDetailFragmentDirections.actionBloodPressureDetailFragmentToDisclaimerFragment())
            }
        }
    }
}