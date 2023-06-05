package com.blood.ui.fragments.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.utils.AdsUtils.BannerUtils.loadBanner
import com.blood.utils.FirebaseUtils
import com.blood.utils.customview.HeaderView
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BR
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentInfoDetailBinding

class InfoDetailFragment : BaseFragment<BaseViewModel, FragmentInfoDetailBinding>(
    R.layout.fragment_info_detail, BaseViewModel::class.java
), HeaderView.Listener {

    val args: InfoDetailFragmentArgs by navArgs()

    override fun init(inflater: LayoutInflater, container: ViewGroup) {
        super.init(inflater, container)
        binding.setVariable(BR.infoDetailFragment, this)
    }

    override fun initData() {
        FirebaseUtils.eventDisplayInfoDetail()
        binding.ivPhoto.setImageResource(args.infoKnowledge.photo)
    }

    override fun initAds() {
        binding.flBanner.loadBanner(
            requireActivity(), BuildConfig.banner_home, prefUtils.isShowBannerHome
        )
    }

    override fun onHeaderBackPressed() {
        FirebaseUtils.eventClickBackInfoPost()
        safeBackNav()
    }
}