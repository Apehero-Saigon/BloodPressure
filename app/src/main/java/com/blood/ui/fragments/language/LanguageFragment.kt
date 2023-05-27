package com.blood.ui.fragments.language

import android.view.View
import androidx.navigation.fragment.findNavController
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.ui.adapters.LanguageAdapter
import com.blood.utils.LanguageUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentLanguageBinding

class LanguageFragment : BaseFragment<BaseViewModel, FragmentLanguageBinding>(
    R.layout.fragment_language, BaseViewModel::class.java
) {
    private lateinit var adapter: LanguageAdapter
    private var isDoneSplash = false

    override fun initData() {
        adapter = LanguageAdapter(requireContext())
        binding.rcvLanguage.adapter = adapter
    }

    override fun initAds() {
        checkShowNativeLanguage()
    }

    override fun initListener() {
        binding.ivConfirmLanguage.clickWithDebounce {
            val chooseLang = adapter.getSelectedLanguage()
            if (chooseLang != null) {
                prefUtils.defaultLanguage = chooseLang.code
                prefUtils.isShowLanguageFirstOpen = false
                LanguageUtils.changeLanguage(requireContext(), chooseLang.code)

                val action = LanguageFragmentDirections.actionLanguageFragmentToOnBoardingFragment()
                findNavController().navigate(action)
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
                requireActivity(),
                BuildConfig.native_language,
                null,
                R.layout.native_medium,
                binding.flAds
            )
        } else {
            binding.flAds.visibility = View.GONE
        }
    }
}