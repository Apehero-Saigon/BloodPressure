package com.blood.ui.fragments.insight

import android.content.Context
import com.blood.base.BaseFragment
import com.blood.ui.fragments.home.HomeFragment
import com.blood.ui.fragments.home.HomeFragmentDirections
import com.blood.ui.fragments.home.IHomeUi
import com.blood.utils.FirebaseUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentInsightBinding

class InsightFragment : BaseFragment<InsightViewModel, FragmentInsightBinding>(
    R.layout.fragment_insight, InsightViewModel::class.java
) {
    var iHomeUi: IHomeUi? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment?.parentFragment is HomeFragment) {
            iHomeUi = parentFragment?.parentFragment as HomeFragment
        }
    }

    override fun initData() {
        super.initData()
        FirebaseUtils.eventDisplayInsightScreen()
        viewModel.checkCount()
    }

    override fun initListener() {
        super.initListener()
        with(binding) {
            llBlood.clickWithDebounce {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToInsightBloodPressureFragment()
                iHomeUi?.navigateTo(action)
            }

            llHeart.clickWithDebounce {

            }

            llWeight.clickWithDebounce {

            }
        }
    }
}