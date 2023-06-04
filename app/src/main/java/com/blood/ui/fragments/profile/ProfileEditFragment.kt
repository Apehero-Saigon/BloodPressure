package com.blood.ui.fragments.profile

import android.graphics.Typeface
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.blood.App
import com.blood.base.BaseFragment
import com.blood.data.Profile
import com.blood.utils.AdsUtils.BannerUtils.loadBanner
import com.blood.utils.DateUtils
import com.blood.utils.FirebaseUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.ViewUtils.gone
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentProfileEditBinding

class ProfileEditFragment : BaseFragment<ProfileViewModel, FragmentProfileEditBinding>(
    R.layout.fragment_profile_edit, ProfileViewModel::class.java
) {

    val args: ProfileEditFragmentArgs by navArgs()

    override fun backPressedWithExitPopup() = args.editMode == false

    override fun initAds() {
        if (isNetworkConnected() && prefUtils.isShowNativeCreateUser) {
            adsUtils.nativeCreateUser.showAds(
                requireActivity(),
                R.layout.layout_native_medium_custom,
                binding.flAds
            )
        } else {
            binding.flAds.gone()
        }

        if (isNetworkConnected() && prefUtils.isShowNativeDefaultValue) {
            App.adsUtils.nativeDefaultValue.loadAds(requireActivity(), R.layout.native_medium)
        }
    }

    override fun initView() {
        FirebaseUtils.eventDisplayCreateUser()
        with(binding) {
            pickBirthDay.setFormatter(R.string.number_picker_formatter)
            pickBirthDay.maxValue = DateUtils.getCurrentYear()
            pickBirthDay.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            pickBirthDay.setSelectedTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
            pickBirthDay.value = 1990

            pickHeight.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            pickHeight.setSelectedTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

            pickWeight.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            pickWeight.setSelectedTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

            tvMale.isSelected = true
        }
    }

    override fun initListener() {
        super.initListener()
        binding.tvMale.setOnClickListener {
            binding.tvMale.isSelected = true
            binding.tvFemale.isSelected = false
        }

        binding.tvFemale.setOnClickListener {
            binding.tvMale.isSelected = false
            binding.tvFemale.isSelected = true
        }

        binding.tvSave.clickWithDebounce {
            createProfile()
        }

        viewModel.insertProfileObserver.observe(this.viewLifecycleOwner) { profile ->
            if (profile != null) {
                prefUtils.profile = profile
                val action =
                    ProfileEditFragmentDirections.actionProfileEditFragmentToMeasurementGuidelineDefaultFragment()
                findNavController().navigate(action)
            } else {
                toast(getString(R.string.cannot_add_profile))
            }
        }
    }

    private fun createProfile() {
        val valueGender = if (binding.tvMale.isSelected) 1 else 2
        val valueWeight = binding.pickWeight.value
        val valueHeight = binding.pickHeight.value
        val valueBirthYear = binding.pickBirthDay.value

        FirebaseUtils.eventClickCreateUser(
            valueBirthYear, if (valueGender == 1) "Male" else "Female"
        )
        val newProfile = Profile(
            if (args.editMode) 0 else 1,
            valueBirthYear,
            valueGender,
            valueHeight,
            valueWeight,
        )

        viewModel.insertProfile(newProfile)
    }
}