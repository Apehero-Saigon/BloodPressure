package com.blood.ui.fragments.insight

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.blood.base.BaseFragment
import com.blood.common.enumdata.FilterType
import com.blood.data.BloodPressure
import com.blood.ui.adapters.BloodPressureAdapter
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.ViewUtils.gone
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BR
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentInsightBloodPressureBinding

class InsightBloodPressureFragment :
    BaseFragment<InsightViewModel, FragmentInsightBloodPressureBinding>(
        R.layout.fragment_insight_blood_pressure, InsightViewModel::class.java
    ), BloodPressureAdapter.Callback {

    companion object {

        private var filterType = FilterType.ALL
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup) {
        super.init(inflater, container)
        binding.setVariable(BR.insightBloodPressureFragment, this)
    }

    override fun initAds() {
        if (isNetworkConnected() && prefUtils.isShowNativeRecentAction) {
            adsUtils.nativeRecentAction.showAds(
                requireActivity(),
                BuildConfig.native_recent_action,
                null,
                R.layout.layout_native_medium_custom,
                binding.flAds,
                false,
                reloadAfterShow = true
            )
        } else {
            binding.flAds.gone()
        }
    }

    override fun initData() {
        updateFilter()
        with(binding) {
            tvAll.clickWithDebounce {
                if (!tvAll.isSelected) {
                    filterType = FilterType.ALL
                    updateFilter(true)
                }
            }

            tvWeek.clickWithDebounce {
                if (!tvWeek.isSelected) {
                    filterType = FilterType.WEEK
                    updateFilter(true)
                }
            }

            tvMonth.clickWithDebounce {
                if (!tvMonth.isSelected) {
                    filterType = FilterType.MONTH
                    updateFilter(true)
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()

        with(binding) {

            btnBack.clickWithDebounce {
                findNavController().navigateUp()
            }

            btnMeasureNow.clickWithDebounce {
                val action =
                    InsightBloodPressureFragmentDirections.actionInsightBloodPressureFragmentToBloodPressureEditFragment()
                action.modeAdd = true
                action.mustShowBackButton = true
                findNavController().navigate(action)
            }
        }
    }

    override fun onClick(data: BloodPressure, position: Int) {
        adsUtils.interBloodDetails.showInterAdsBeforeNavigate(requireContext(), true) {
            val action =
                InsightBloodPressureFragmentDirections.actionInsightBloodPressureFragmentToBloodPressureDetailFragment()
            action.id = data.id
            action.viewDetail = true
            findNavController().navigate(action)
        }
    }

    private fun updateFilter(withLoading: Boolean = false) {
        with(binding) {
            tvAll.isSelected = filterType == FilterType.ALL
            tvAll.setTextColor(
                ContextCompat.getColor(
                    requireContext(), if (tvAll.isSelected) R.color.white else R.color.black
                )
            )

            tvWeek.isSelected = filterType == FilterType.WEEK
            tvWeek.setTextColor(
                ContextCompat.getColor(
                    requireContext(), if (tvWeek.isSelected) R.color.white else R.color.black
                )
            )

            tvMonth.isSelected = filterType == FilterType.MONTH
            tvMonth.setTextColor(
                ContextCompat.getColor(
                    requireContext(), if (tvMonth.isSelected) R.color.white else R.color.black
                )
            )
        }
        viewModel.loadListBlood(filterType, withLoading)
    }
}