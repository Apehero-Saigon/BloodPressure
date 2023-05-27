package com.blood.ui.fragments.info

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.utils.AdsUtils.BannerUtils.loadBanner
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentInfoDetailBinding

class InfoDetailFragment : BaseFragment<BaseViewModel, FragmentInfoDetailBinding>(
    R.layout.fragment_info_detail, BaseViewModel::class.java
) {

    val args: InfoDetailFragmentArgs by navArgs()

    override fun initData() {
        binding.ivPhoto.setImageResource(args.infoKnowledge.photo)
    }

    override fun initAds() {
        binding.flBanner.loadBanner(
            requireActivity(), BuildConfig.banner_home, prefUtils.isShowBannerHome
        )
    }

    override fun initListener() {
        super.initListener()
        binding.btnBack.clickWithDebounce {
            findNavController().navigateUp()
        }
    }
}