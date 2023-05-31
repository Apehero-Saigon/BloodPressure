package com.blood.ui.fragments.insight

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.blood.base.BaseFragment
import com.blood.common.enumdata.FilterType
import com.blood.data.BloodPressure
import com.blood.ui.adapters.BloodPressureAdapter
import com.blood.ui.fragments.home.HomeFragment
import com.blood.ui.fragments.home.HomeFragmentDirections
import com.blood.ui.fragments.home.IHomeUi
import com.blood.utils.FirebaseUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.customview.HeaderView
import com.blood.utils.customview.chart.CandleChart
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BR
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentInsightBloodPressureBinding

class InsightBloodPressureFragment :
    BaseFragment<InsightViewModel, FragmentInsightBloodPressureBinding>(
        R.layout.fragment_insight_blood_pressure, InsightViewModel::class.java
    ), BloodPressureAdapter.Callback, HeaderView.Listener {

    companion object {

        private var filterType = FilterType.ALL
    }


    var iHomeUi: IHomeUi? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment?.parentFragment is HomeFragment) {
            iHomeUi = parentFragment?.parentFragment as HomeFragment
        }
    }

    override fun init(inflater: LayoutInflater, container: ViewGroup) {
        super.init(inflater, container)
        binding.setVariable(BR.insightBloodPressureFragment, this)
    }

    override fun initData() {
        FirebaseUtils.eventDisplayInsightBlood()
        updateFilter()
        with(binding) {
            tvAll.clickWithDebounce {
                if (!tvAll.isSelected) {
                    filterType = FilterType.ALL
                    updateFilter(true)
                    FirebaseUtils.eventClickChooseDateInsightBlood()
                }
            }

            tvWeek.clickWithDebounce {
                if (!tvWeek.isSelected) {
                    filterType = FilterType.WEEK
                    updateFilter(true)
                    FirebaseUtils.eventClickChooseDateInsightBlood()
                }
            }

            tvMonth.clickWithDebounce {
                if (!tvMonth.isSelected) {
                    filterType = FilterType.MONTH
                    updateFilter(true)
                    FirebaseUtils.eventClickChooseDateInsightBlood()
                }
            }

            tvForMore.clickWithDebounce {
                iHomeUi?.navigateTo(HomeFragmentDirections.actionHomeFragmentToMeasurementGuidelineFragment())
            }
        }
    }

    override fun initListener() {
        super.initListener()
        viewModel.listBloodObserver.observe(this.viewLifecycleOwner) { list ->
            val chartData = mutableListOf<CandleChart.Data>()
            list.forEach {
                chartData.add(
                    CandleChart.Data(
                        it.systole.toFloat(), it.diastole.toFloat(), it.createAt
                    )
                )
            }
            binding.candleChart.setData(
                chartData,
                if (chartData.isEmpty()) 0f else chartData.maxOf { it.max },
                if (chartData.isEmpty()) 0f else chartData.minOf { it.min },
            )
        }

        with(binding) {
            btnMeasureNow.clickWithDebounce {
                val action = HomeFragmentDirections.actionHomeFragmentToBloodPressureEditFragment()
                action.modeAdd = true
                action.mustShowBackButton = true
                iHomeUi?.navigateTo(action)
            }
        }
    }

    override fun onClick(data: BloodPressure, position: Int) {
        FirebaseUtils.eventClickDetailItemInsightBlood()
        adsUtils.interBloodDetails.showInterAdsBeforeNavigate(requireContext(), true) {
            val action = HomeFragmentDirections.actionHomeFragmentToBloodPressureDetailFragment()
            action.id = data.id
            action.viewDetail = true
            iHomeUi?.navigateTo(action)
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