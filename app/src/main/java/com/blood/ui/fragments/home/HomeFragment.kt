package com.blood.ui.fragments.home

import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentHomeBinding
import com.blood.base.BaseFragment
import com.blood.utils.AdsUtils.BannerUtils.loadBanner
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(
    R.layout.fragment_home, HomeViewModel::class.java
), IHomeUi {

    override fun initAds() {
        binding.flBanner.loadBanner(
            requireActivity(), BuildConfig.banner_home, prefUtils.isShowBannerHome
        )

        adsUtils.interInfo.isShowHighAds = prefUtils.isShowInterInfoHigh
        adsUtils.interInfo.isShowNormalAds = prefUtils.isShowInterInfo
        adsUtils.interInfo.loadInterPrioritySameTime(
            requireContext(),
            BuildConfig.inter_info_high,
            BuildConfig.inter_info
        )
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
        findNavController().navigate(action)
    }
}