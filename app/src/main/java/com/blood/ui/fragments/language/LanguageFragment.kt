package com.blood.ui.fragments.language

import android.view.View
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.ui.adapters.LanguageAdapter
import com.blood.utils.FirebaseUtils
import com.blood.utils.LanguageUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentLanguageBinding

class LanguageFragment : BaseFragment<BaseViewModel, FragmentLanguageBinding>(
    R.layout.fragment_language, BaseViewModel::class.java
) {
    private lateinit var adapter: LanguageAdapter
    private var isDoneSplash = false

    override fun backPressedWithExitPopup() = true

    override fun initData() {
        FirebaseUtils.eventDisplayLanguageScreen()
        adapter = LanguageAdapter(LanguageUtils.languageListItems(requireContext()).apply {
            get(0).isChoose = true
        })
        binding.rcvLanguage.adapter = adapter
    }

    override fun initAds() {
        checkShowNativeLanguage()
    }

    override fun initListener() {
        super.initListener()
        binding.ivConfirmLanguage.clickWithDebounce {
            val chooseLang = adapter.getSelectedLanguage()
            if (chooseLang != null) {
                FirebaseUtils.eventClickChooseLanguage()
                prefUtils.defaultLanguage = chooseLang.code
                prefUtils.isShowLanguageFirstOpen = false
                LanguageUtils.loadLocale(requireContext(), chooseLang.code)

                val action = LanguageFragmentDirections.actionLanguageFragmentToOnBoardingFragment()
                safeNav(action)
            } else {
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isDoneSplash = true
    }

    private fun checkShowNativeLanguage() {
        if (isNetworkConnected() && prefUtils.isShowNativeLanguage) {
            adsUtils.nativeLanguage.showAds(
                requireActivity(), R.layout.native_medium, binding.flAds, waitForNewAds = true
            )
        } else {
            binding.flAds.visibility = View.GONE
        }
    }
}