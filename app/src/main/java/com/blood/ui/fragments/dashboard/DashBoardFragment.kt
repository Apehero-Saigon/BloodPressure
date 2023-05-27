package com.blood.ui.fragments.dashboard

import android.content.Context
import androidx.navigation.fragment.findNavController
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentDashboardBinding
import com.blood.base.BaseFragment
import com.blood.common.Constant
import com.blood.data.BloodPressure
import com.blood.ui.dialog.SaveBloodPressurePopup
import com.blood.ui.fragments.home.HomeFragment
import com.blood.ui.fragments.home.HomeFragmentDirections
import com.blood.ui.fragments.home.HomeViewModel
import com.blood.ui.fragments.home.IHomeUi
import com.blood.ui.fragments.profile.ProfileEditFragmentDirections
import com.blood.utils.DateUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.ViewUtils.textTrim

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
        super.initListener()
        viewModel.insertBloodPressureObserver.observe(this.viewLifecycleOwner) { profile ->
            if (profile != null) {
                adsUtils.interMeasure.showInterAdsBeforeNavigate(requireContext(), true) {
                    iHomeUi?.navigateTo(HomeFragmentDirections.actionHomeFragmentToBloodPressureDetailFragment())
                }
            } else {
                toast(getString(R.string.cannot_add_blood_pressure))
            }
        }

        binding.btnSave.clickWithDebounce {
            val dateStr = "${binding.tvDate.text} ${binding.tvDate.text}"
            val date = DateUtils.format(dateStr, Constant.FORMAT_DATETIME)

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
    }
}