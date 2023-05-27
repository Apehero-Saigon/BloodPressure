package com.blood.ui.fragments.insight

import android.content.Context
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentInsightBinding
import com.blood.base.BaseFragment
import com.blood.ui.fragments.home.HomeFragment
import com.blood.ui.fragments.home.HomeViewModel
import com.blood.ui.fragments.home.IHomeUi

class InsightFragment : BaseFragment<HomeViewModel, FragmentInsightBinding>(
    R.layout.fragment_insight, HomeViewModel::class.java
) {
    var iHomeUi: IHomeUi? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment?.parentFragment is HomeFragment) {
            iHomeUi = parentFragment?.parentFragment as HomeFragment
        }
    }
}