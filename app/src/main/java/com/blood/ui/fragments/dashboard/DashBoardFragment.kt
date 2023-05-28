package com.blood.ui.fragments.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.blood.base.BaseFragment
import com.blood.common.Constant
import com.blood.data.BloodPressure
import com.blood.ui.adapters.BloodPressureAdapter
import com.blood.ui.dialog.SaveBloodPressurePopup
import com.blood.ui.fragments.bloodpressure.BloodPressureViewModel
import com.blood.ui.fragments.home.HomeFragment
import com.blood.ui.fragments.home.HomeFragmentDirections
import com.blood.ui.fragments.home.IHomeUi
import com.blood.utils.DateUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.ViewUtils.textTrim
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BR
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentDashboardBinding
import java.util.Date

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
        binding.setVariable(BR.bloodListener, this)
    }

    override fun initView() {
        with(binding) {
            tvDate.text = DateUtils.getDateStr(System.currentTimeMillis(), Constant.FORMAT_DATE)
            tvTime.text = DateUtils.getDateStr(System.currentTimeMillis(), Constant.FORMAT_TIME)
        }
    }

    override fun initData() {
        super.initData()
        viewModel.getListBloodPressure(3)
    }

    override fun initListener() {
        super.initListener()
        viewModel.insertBloodPressureObserver.observe(this.viewLifecycleOwner) { bloodPressure ->
            if (bloodPressure != null) {
                adsUtils.interMeasure.showInterAdsBeforeNavigate(requireContext(), true) {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToBloodPressureDetailFragment()
                    action.id = bloodPressure.id
                    action.viewDetail = false
                    iHomeUi?.navigateTo(action)
                }
            } else {
                toast(getString(R.string.cannot_add_blood_pressure))
            }
        }

        binding.btnSave.clickWithDebounce {
            val dateStr = getStringDateTime()
            val date = DateUtils.format(dateStr, Constant.FORMAT_DATETIME) ?: Date()

            val bloodPressure = BloodPressure(
                profileId = prefUtils.profile!!.id,
                systole = binding.pickSys.value,
                diastole = binding.pickDia.value,
                pulse = binding.pickPurse.value,
                note = binding.edtNote.textTrim(),
                createAt = date
            )

            SaveBloodPressurePopup.showPopup(
                childFragmentManager,
                bloodPressure.systole,
                bloodPressure.diastole,
                bloodPressure.pulse
            ) {
                viewModel.insertBloodPressure(bloodPressure)
            }
        }

        binding.tvDate.clickWithDebounce {
            DateUtils.openDatePicker(requireContext(),
                DateUtils.format(getStringDateTime(), Constant.FORMAT_DATETIME),
                object : DateUtils.SelectDatetimeListener {
                    override fun onDateSelected(day: Int, month: Int, year: Int) {
                        binding.tvDate.text = getString(R.string.dd_mm_yyyy, day, month, year)
                    }
                })
        }

        binding.tvTime.clickWithDebounce {
            DateUtils.openTimePicker(requireContext(),
                DateUtils.format(getStringDateTime(), Constant.FORMAT_DATETIME),
                object : DateUtils.SelectDatetimeListener {
                    override fun onTimeSelected(minute: Int, hour: Int) {
                        binding.tvTime.text = DateUtils.strTime(hour, minute)
                    }
                })
        }
    }

    private fun getStringDateTime() = "${binding.tvDate.text} ${binding.tvTime.text}"
    override fun onClick(data: BloodPressure, position: Int) {
        toast(getString(data.getStatus()))
    }
}