package com.blood.ui.fragments.bloodpressure

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.blood.base.BaseFragment
import com.blood.common.Constant
import com.blood.data.BloodPressure
import com.blood.ui.dialog.SaveBloodPressurePopup
import com.blood.ui.fragments.dashboard.DashBoardFragment
import com.blood.ui.fragments.home.HomeFragmentDirections
import com.blood.ui.fragments.home.IHomeUi
import com.blood.utils.AppUtils.isNotNull
import com.blood.utils.DateUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.ViewUtils.gone
import com.blood.utils.customview.HeaderView
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BR
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentBloodPressureEditBinding
import java.util.Date

class BloodPressureEditFragment : BaseFragment<BloodPressureViewModel, FragmentBloodPressureEditBinding>(
    R.layout.fragment_blood_pressure_edit, BloodPressureViewModel::class.java
), HeaderView.Listener {

    private var iHomeUi: IHomeUi? = null

    val args: BloodPressureEditFragmentArgs by navArgs()

    override fun init(inflater: LayoutInflater, container: ViewGroup) {
        super.init(inflater, container)
        binding.setVariable(BR.bloodPressureEditFragment, this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        iHomeUi = (parentFragment?.parentFragment as? DashBoardFragment)?.iHomeUi
    }

    override fun initAds() {
        adsUtils.nativeBloodPressure.showAds(requireActivity(), binding.flAds, reloadAfterShow = true)
    }

    override fun initView() {
        with(binding) {
            header.binding.tvHeader.setText(if (args.modeAdd) R.string.add_your_record else R.string.edit_your_record)

            pickSys.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            pickSys.setSelectedTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

            pickDia.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            pickDia.setSelectedTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

            pickPurse.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            pickPurse.setSelectedTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        }
    }

    override fun initData() {
        binding.btnSave.setText(R.string.save)
        if (args.modeAdd) {
            resetData()
        } else {
            viewModel.getBloodPressureByID(args.id)
        }
    }

    override fun initListener() {
        super.initListener()
        viewModel.updateBloodPressureObserver.observe(this.viewLifecycleOwner) { bloodPressure ->
            if (bloodPressure != null) {
                adsUtils.interSave.showInterAdsBeforeNavigate(requireContext(), true) {
                    if (args.modeAdd) {
                        resetData()
                        if (iHomeUi.isNotNull()) {
                            val action = HomeFragmentDirections.actionHomeFragmentToBloodPressureDetailFragment()
                            action.id = bloodPressure.id
                            action.viewDetail = false
                            iHomeUi?.navigateTo(action)
                        } else {
                            val action = BloodPressureEditFragmentDirections.actionBloodPressureEditFragmentToBloodPressureDetailFragment()
                            action.id = bloodPressure.id
                            action.viewDetail = false
                            safeNav(action)
                        }
                    } else {
                        safeBackNav()
                    }
                }
            } else {
                toast(getString(R.string.cannot_add_blood_pressure))
            }
        }

        with(binding) {
            btnSave.clickWithDebounce {
                updateBloodPressure()
            }

            tvDate.clickWithDebounce {
                DateUtils.openDatePicker(
                    requireContext(),
                    DateUtils.format(getStringDateTime(), Constant.FORMAT_DATETIME),
                    object : DateUtils.SelectDatetimeListener {
                        override fun onDateSelected(day: Int, month: Int, year: Int) {
                            binding.tvDate.text = getString(R.string.dd_mm_yyyy, day, month, year)
                        }
                    })
            }

            tvTime.clickWithDebounce {
                DateUtils.openTimePicker(
                    requireContext(),
                    DateUtils.format(getStringDateTime(), Constant.FORMAT_DATETIME),
                    object : DateUtils.SelectDatetimeListener {
                        override fun onTimeSelected(minute: Int, hour: Int) {
                            binding.tvTime.text = DateUtils.strTime(hour, minute)
                        }
                    })
            }
        }
    }

    override fun onHeaderBackPressed() {
        safeBackNav()
    }

    private fun updateBloodPressure() {
        val dateStr = getStringDateTime()
        val date = DateUtils.format(dateStr, Constant.FORMAT_DATETIME) ?: Date()

        val bloodPressure = BloodPressure(
            profileId = Constant.PROFILE_ID_DEFAULT,
            systole = binding.pickSys.value,
            diastole = binding.pickDia.value,
            pulse = binding.pickPurse.value,
            createAt = date,
            id = if (args.modeAdd) 0 else args.id
        )

        SaveBloodPressurePopup.showPopup(
            childFragmentManager,
            bloodPressure.systole,
            bloodPressure.diastole,
            bloodPressure.pulse,
            viewModel.bloodPressureObserver.value?.note ?: ""
        ) { note ->
            bloodPressure.note = note
            viewModel.updateBloodPressure(bloodPressure)
        }
    }

    private fun resetData() {
        with(binding) {
            tvDate.text = DateUtils.getDateStr(System.currentTimeMillis(), Constant.FORMAT_DATE)
            tvTime.post {
                tvTime.text = DateUtils.getDateStr(System.currentTimeMillis(), Constant.FORMAT_TIME)
            }
        }
    }

    private fun getStringDateTime() = "${binding.tvDate.text} ${binding.tvTime.text}"
}