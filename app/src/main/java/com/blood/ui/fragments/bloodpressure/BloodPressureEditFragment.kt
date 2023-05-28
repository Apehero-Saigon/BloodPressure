package com.blood.ui.fragments.bloodpressure

import android.content.Context
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.blood.base.BaseFragment
import com.blood.common.Constant
import com.blood.data.BloodPressure
import com.blood.ui.dialog.SaveBloodPressurePopup
import com.blood.ui.fragments.dashboard.DashBoardFragment
import com.blood.ui.fragments.home.HomeFragmentDirections
import com.blood.ui.fragments.home.IHomeUi
import com.blood.utils.AdsUtils.BannerUtils.loadBanner
import com.blood.utils.AppUtils.isNull
import com.blood.utils.DateUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.ViewUtils.textTrim
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentBloodPressureEditBinding
import java.util.Date

class BloodPressureEditFragment :
    BaseFragment<BloodPressureViewModel, FragmentBloodPressureEditBinding>(
        R.layout.fragment_blood_pressure_edit, BloodPressureViewModel::class.java
    ) {

    private var iHomeUi: IHomeUi? = null

    val args: BloodPressureEditFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        iHomeUi = (parentFragment?.parentFragment as? DashBoardFragment)?.iHomeUi
    }

    override fun initAds() {
        binding.flBanner.loadBanner(
            requireActivity(),
            BuildConfig.banner_home,
            args.mustShowBackButton && prefUtils.isShowBannerHome && isNetworkConnected()
        )
    }

    override fun initData() {
        if (args.modeAdd) {
            resetData()
            binding.tvTitle.setText(R.string.add_your_record)
            binding.btnSave.setText(R.string.save)
        } else {
            binding.tvTitle.setText(R.string.edit_your_record)
            binding.btnSave.setText(R.string.save)

            viewModel.getBloodPressureByID(args.id)
        }
    }

    override fun initListener() {
        super.initListener()
        viewModel.updateBloodPressureObserver.observe(this.viewLifecycleOwner) { bloodPressure ->
            if (bloodPressure != null) {
                adsUtils.interMeasure.showInterAdsBeforeNavigate(requireContext(), true) {
                    if (args.modeAdd) {
                        resetData()
                        if (findNavController().previousBackStackEntry.isNull()) {
                            val action =
                                HomeFragmentDirections.actionHomeFragmentToBloodPressureDetailFragment()
                            action.id = bloodPressure.id
                            action.viewDetail = false
                            iHomeUi?.navigateTo(action)
                        } else {
                            val action =
                                BloodPressureEditFragmentDirections.actionBloodPressureEditFragmentToBloodPressureDetailFragment()
                            action.id = bloodPressure.id
                            action.viewDetail = false
                            findNavController().navigate(action)
                        }
                    } else {
                        findNavController().navigateUp()
                    }
                }
            } else {
                toast(getString(R.string.cannot_add_blood_pressure))
            }
        }

        binding.btnSave.clickWithDebounce {
            updateBloodPressure()
        }

        binding.btnBack.clickWithDebounce {
            findNavController().navigateUp()
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

    private fun updateBloodPressure() {
        val dateStr = getStringDateTime()
        val date = DateUtils.format(dateStr, Constant.FORMAT_DATETIME) ?: Date()

        val bloodPressure = BloodPressure(
            profileId = prefUtils.profile!!.id,
            systole = binding.pickSys.value,
            diastole = binding.pickDia.value,
            pulse = binding.pickPurse.value,
            note = binding.edtNote.textTrim(),
            createAt = date,
            id = if (args.modeAdd) 0 else args.id
        )

        SaveBloodPressurePopup.showPopup(
            childFragmentManager, bloodPressure.systole, bloodPressure.diastole, bloodPressure.pulse
        ) {
            viewModel.updateBloodPressure(bloodPressure)
        }
    }

    private fun resetData() {
        with(binding) {
            edtNote.setText("")
            tvDate.text = DateUtils.getDateStr(System.currentTimeMillis(), Constant.FORMAT_DATE)
            tvTime.text = DateUtils.getDateStr(System.currentTimeMillis(), Constant.FORMAT_TIME)
        }
    }

    private fun getStringDateTime() = "${binding.tvDate.text} ${binding.tvTime.text}"
}