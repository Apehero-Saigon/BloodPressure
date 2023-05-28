package com.blood.ui.fragments.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.blood.base.BaseFragment
import com.blood.data.BloodPressure
import com.blood.ui.adapters.BloodPressureAdapter
import com.blood.ui.fragments.bloodpressure.BloodPressureViewModel
import com.blood.ui.fragments.home.HomeFragment
import com.blood.ui.fragments.home.HomeFragmentDirections
import com.blood.ui.fragments.home.IHomeUi
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BR
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentDashboardBinding

class DashBoardFragment : BaseFragment<BloodPressureViewModel, FragmentDashboardBinding>(
    R.layout.fragment_dashboard, BloodPressureViewModel::class.java
), BloodPressureAdapter.Callback {
    var iHomeUi: IHomeUi? = null

    override fun loadingText() = getString(R.string.saving)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment?.parentFragment is HomeFragment) {
            iHomeUi = parentFragment?.parentFragment as HomeFragment
        }
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup) {
        super.init(inflater, container)
        binding.setVariable(BR.dashBoardFragment, this)
    }

    override fun initData() {
        super.initData()
        viewModel.getListBloodPressure(3)
    }

    override fun onClick(data: BloodPressure, position: Int) {
        adsUtils.interBloodDetails.showInterAdsBeforeNavigate(requireContext(), true) {
            val action = HomeFragmentDirections.actionHomeFragmentToBloodPressureDetailFragment()
            action.id = data.id
            action.viewDetail = true
            iHomeUi?.navigateTo(action)
        }
    }
}