package com.blood.ui.fragments.home

import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.blood.base.BaseFragment
import com.blood.utils.AdsUtils.BannerUtils.loadBanner
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>(
    R.layout.fragment_home, HomeViewModel::class.java
), IHomeUi {

    override fun backPressedWithExitPopup() = true

    override fun initAds() {
        binding.flBanner.loadBanner(
            requireActivity(), BuildConfig.banner_home, prefUtils.isShowBannerHome
        )

        adsUtils.interSave.isShowHighAds = prefUtils.isShowInterSaveHigh
        adsUtils.interSave.isShowNormalAds = prefUtils.isShowInterSave
        adsUtils.interSave.loadInterPrioritySameTime(requireContext())

        adsUtils.interInsightDetail.isShowHighAds = prefUtils.isShowInterInsightDetailHigh
        adsUtils.interInsightDetail.isShowNormalAds = prefUtils.isShowInterInsightDetail
        adsUtils.interInsightDetail.loadInterPrioritySameTime(requireContext())

        adsUtils.interInfo.isShowHighAds = prefUtils.isShowInterInfoHigh
        adsUtils.interInfo.isShowNormalAds = prefUtils.isShowInterInfo
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