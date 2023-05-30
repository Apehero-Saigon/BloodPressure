package com.blood.ui.fragments.setting

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.ads.control.admob.AppOpenManager
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentSettingBinding
import com.blood.base.BaseFragment
import com.blood.base.recyclerview.BaseRecyclerViewListener
import com.blood.common.Constant
import com.blood.data.SettingMenu
import com.blood.ui.adapters.SettingAdapter
import com.blood.ui.fragments.home.HomeFragment
import com.blood.ui.fragments.home.HomeFragmentDirections
import com.blood.ui.fragments.home.HomeViewModel
import com.blood.ui.fragments.home.IHomeUi
import com.blood.utils.AppUtils

class SettingFragment : BaseFragment<HomeViewModel, FragmentSettingBinding>(
    R.layout.fragment_setting, HomeViewModel::class.java
), BaseRecyclerViewListener<SettingMenu> {

    var iHomeUi: IHomeUi? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment?.parentFragment is HomeFragment) {
            iHomeUi = parentFragment?.parentFragment as HomeFragment
        }
    }

    override fun initView() {
        val adapter = SettingAdapter(SettingMenu.getList(), this)
        val manager = LinearLayoutManager(requireContext())

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = manager
    }

    override fun onClick(data: SettingMenu, position: Int) {
        when (data.type) {
            SettingMenu.Companion.Type.LANGUAGE -> {
                iHomeUi?.navigateTo(HomeFragmentDirections.actionHomeFragmentToLanguageSettingFragment())
            }

            SettingMenu.Companion.Type.PRIVACY -> {
                AppOpenManager.getInstance().disableAdResumeByClickAction()
                AppUtils.openWebsite(requireContext(), Constant.URL_PRIVACY)
            }

            SettingMenu.Companion.Type.TERM -> {
                AppOpenManager.getInstance().disableAdResumeByClickAction()
                AppUtils.openWebsite(requireContext(), Constant.URL_TERM_OF_SERVICE)
            }

            SettingMenu.Companion.Type.DISCLAIMER -> {
                iHomeUi?.navigateTo(HomeFragmentDirections.actionHomeFragmentToDisclaimerFragment())
            }

            SettingMenu.Companion.Type.LIMIT_VALUES -> {
                iHomeUi?.navigateTo(HomeFragmentDirections.actionHomeFragmentToLimitValuesFragment())
            }

            else -> {

            }
        }
    }
}