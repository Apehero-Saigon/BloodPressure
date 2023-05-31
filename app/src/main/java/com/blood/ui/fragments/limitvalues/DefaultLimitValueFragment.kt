package com.blood.ui.fragments.limitvalues

import androidx.navigation.fragment.findNavController
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentLimitValuesDefaultBinding

class DefaultLimitValueFragment : BaseFragment<BaseViewModel, FragmentLimitValuesDefaultBinding>(
    R.layout.fragment_limit_values_default, BaseViewModel::class.java
) {

    override fun initData() {
        super.initData()
        updateTypeSelected(false)
        with(binding) {

            cl2018.clickWithDebounce {
                updateTypeSelected(false)
            }

            cl2017.clickWithDebounce {
                updateTypeSelected(true)
            }

            btnOk.clickWithDebounce {
                val action =
                    DefaultLimitValueFragmentDirections.actionDefaultLimitValueFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun updateTypeSelected(is2017Value: Boolean) {
        binding.cl2017.isSelected = is2017Value
        binding.cl2018.isSelected = !is2017Value
    }
}