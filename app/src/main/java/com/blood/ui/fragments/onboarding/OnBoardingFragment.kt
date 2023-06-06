package com.blood.ui.fragments.onboarding

import android.view.View
import com.blood.App
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.ui.adapters.OnBoardingPageAdapter
import com.blood.utils.FirebaseUtils
import com.blood.utils.ViewUtils.autoScroll
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.customview.transformers.ZoomOutSlideTransformer
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentOnboardingBinding

class OnBoardingFragment : BaseFragment<BaseViewModel, FragmentOnboardingBinding>(
    R.layout.fragment_onboarding, BaseViewModel::class.java
) {

    var adapter: OnBoardingPageAdapter? = null

    override fun backPressedWithExitPopup() = true

    override fun initAds() {
        FirebaseUtils.eventDisplayOnBoarding()
        if (isNetworkConnected() && prefUtils.isShowNativeOnBoarding) {
            adsUtils.nativeOnBoarding.showAds(
                requireActivity(), R.layout.native_medium, binding.flAds
            )
        } else {
            binding.flAds.visibility = View.GONE
        }

        if (isNetworkConnected() && prefUtils.isShowNativeBloodPressure) {
            App.adsUtils.nativeBloodPressure.loadAds(requireActivity(), R.layout.native_medium)
        }
    }

    override fun initView() {
        adapter = OnBoardingPageAdapter(requireContext(), childFragmentManager)
        binding.viewPager.autoScroll(1500)
        binding.viewPager.setPageTransformer(true, ZoomOutSlideTransformer())
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun initListener() {
        super.initListener()
        binding.tvStart.clickWithDebounce {
            prefUtils.isShowOnBoardingFirstOpen = false
            FirebaseUtils.eventClickStartNowOnBoard()

            val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToHomeFragment()
            safeNav(action)
        }
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        super.onDestroyView()
    }
}