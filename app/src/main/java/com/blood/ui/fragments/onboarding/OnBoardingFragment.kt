package com.blood.ui.fragments.onboarding

import android.annotation.SuppressLint
import android.view.View
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentOnboardingBinding
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.ui.adapters.OnBoardingPageAdapter
import com.blood.utils.FirebaseUtils
import com.blood.utils.ViewUtils.clickWithDebounce

class OnBoardingFragment : BaseFragment<BaseViewModel, FragmentOnboardingBinding>(
    R.layout.fragment_onboarding, BaseViewModel::class.java
) {

    var adapter: OnBoardingPageAdapter? = null
    private val displayedPage = HashSet<Int>()

    override fun initAds() {
        displayedPage.add(OnBoardingPageAdapter.PAGE_INDEX_1)
        showNativeOnBoarding(OnBoardingPageAdapter.PAGE_INDEX_1)
        FirebaseUtils.eventDisplayOnBoarding1()

        adsUtils.interSaveProfile.loadInterPrioritySameTime(
            requireContext(), BuildConfig.inter_save_high, BuildConfig.inter_save
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView() {
        adapter = OnBoardingPageAdapter(requireContext(), childFragmentManager)
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun initListener() {
        super.initListener()
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == OnBoardingPageAdapter.NUMBER_PAGE - 1) {
                    binding.tvNext.text = getString(R.string.start)
                } else {
                    binding.tvNext.text = getString(R.string.next)
                }

                if (position == 1 && !displayedPage.contains(1)) {
                    displayedPage.add(1)
                    showNativeOnBoarding(position)
                    FirebaseUtils.eventDisplayOnBoarding2()
                } else if (position == 2 && !displayedPage.contains(2)) {
                    displayedPage.add(2)
                    showNativeOnBoarding(position)
                    FirebaseUtils.eventDisplayOnBoarding3()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        binding.tvNext.clickWithDebounce {
            if (binding.viewPager.currentItem == OnBoardingPageAdapter.NUMBER_PAGE - 1) {
                prefUtils.isShowOnBoardingFirstOpen = false
                FirebaseUtils.eventClickStartNowOnBoard()

                val action =
                    OnBoardingFragmentDirections.actionOnBoardingFragmentToProfileEditFragment()
                action.allowBack = false
                action.editMode = false
                findNavController().navigate(action)
            } else {
                binding.viewPager.currentItem++
            }
        }
    }

    override fun onDestroyView() {
        binding.viewPager.adapter = null
        super.onDestroyView()
    }

    private fun showNativeOnBoarding(page: Int) {
        if (isNetworkConnected() && prefUtils.isShowNativeOnBoarding) {
            when (page) {
                0 -> adsUtils.nativeOnBoarding1
                1 -> adsUtils.nativeOnBoarding2
                else -> adsUtils.nativeOnBoarding3
            }.showAds(
                requireActivity(),
                BuildConfig.native_onboarding,
                null,
                null,
                R.layout.native_medium,
                binding.flAds
            )
        } else {
            binding.flAds.visibility = View.GONE
        }
    }
}