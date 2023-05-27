package com.blood.ui.fragments.dashboard

import android.content.Context
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentDashboardBinding
import com.blood.base.BaseFragment
import com.blood.common.Constant
import com.blood.ui.fragments.home.HomeFragment
import com.blood.ui.fragments.home.HomeViewModel
import com.blood.ui.fragments.home.IHomeUi
import com.blood.utils.DateUtils
import com.blood.utils.ViewUtils.clickWithDebounce

class DashBoardFragment : BaseFragment<HomeViewModel, FragmentDashboardBinding>(
    R.layout.fragment_dashboard, HomeViewModel::class.java
) {
    var iHomeUi: IHomeUi? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment?.parentFragment is HomeFragment) {
            iHomeUi = parentFragment?.parentFragment as HomeFragment
        }
    }

    override fun initView() {
        with(binding) {
            tvDate.text = DateUtils.getDateStr(System.currentTimeMillis(), Constant.FORMAT_DATE)
            tvTime.text = DateUtils.getDateStr(System.currentTimeMillis(), Constant.FORMAT_TIME)
        }
    }

    override fun initListener() {
        binding.btnSave.clickWithDebounce {
            val dateStr = "${binding.tvDate.text} ${binding.tvDate.text}"
            val date = DateUtils.format(dateStr, Constant.FORMAT_DATETIME)

//            val record = BloodPressureEntity(profileId, systole, diastole, pulse, time)
        }
    }
}