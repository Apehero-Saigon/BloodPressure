package com.blood.ui.fragments.home

import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.blood.base.BaseFragment
import com.blood.utils.AdsUtils.BannerUtils.Companion.loadBanner
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(
    R.layout.fragment_home, HomeViewModel::class.java
), IHomeUi {

    override fun backPressedWithExitPopup() = true

    override fun initAds() {
        binding.flBanner.loadBanner(requireActivity(), BuildConfig.banner_home, prefUtils.isShowBannerHome)

//        adsUtils.banner.showAds(requireActivity(), binding.flBanner)

        adsUtils.interSave.loadInterPrioritySameTime(requireContext())

        adsUtils.interInsightDetail.loadInterPrioritySameTime(requireContext())

        adsUtils.interInfo.loadInterPrioritySameTime(requireContext())
    }

    override fun initView() {
        settBottomNavigation()
    }

    private fun settBottomNavigation() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun navigateTo(action: NavDirections) {
        safeNav(action)
    }
}