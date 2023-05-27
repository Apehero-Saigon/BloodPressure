package com.blood.ui.fragments.profile

import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.blood.base.BaseFragment
import com.blood.data.Profile
import com.blood.utils.AdsUtils.BannerUtils.loadBanner
import com.blood.utils.DateUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.ViewUtils.gone
import com.blood.utils.ViewUtils.intNumber
import com.blood.utils.ViewUtils.isBlank
import com.blood.utils.ViewUtils.mustRemoveZeroFirst
import com.blood.utils.ViewUtils.textSelected
import com.blood.utils.ViewUtils.textTrim
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentProfileEditBinding

class ProfileEditFragment : BaseFragment<ProfileViewModel, FragmentProfileEditBinding>(
    R.layout.fragment_profile_edit, ProfileViewModel::class.java
) {

    val args: ProfileEditFragmentArgs by navArgs()

    override fun initAds() {
        if (isNetworkConnected() && prefUtils.isShowBannerCreateUser) {
            binding.flBanner.loadBanner(requireActivity(), BuildConfig.banner_create_user)
        } else {
            binding.flBanner.gone()
        }
    }

    override fun initView() {
        // gender
        val genderArray: ArrayList<String> = ArrayList()
        genderArray.add(getString(R.string.male))
        genderArray.add(getString(R.string.female))
        val adapterGender = ArrayAdapter(
            requireContext(), R.layout.spinner_selected, genderArray
        )
        adapterGender.setDropDownViewResource(R.layout.spinner_dropdown)
        binding.sprGender.adapter = adapterGender

        // birthday
        val yearArray: ArrayList<String> = ArrayList()
        for (element in DateUtils.getCurrentYear() downTo 1900) {
            val year = element.toString()
            yearArray.add(year)
        }
        val adapterBirthday = ArrayAdapter(
            requireContext(), R.layout.spinner_selected, yearArray
        )
        adapterBirthday.setDropDownViewResource(R.layout.spinner_dropdown)
        binding.sprYearOfBirth.adapter = adapterBirthday

        binding.edtHeight.mustRemoveZeroFirst()
        binding.edtWeight.mustRemoveZeroFirst()
    }

    override fun initListener() {
        binding.btnSave.clickWithDebounce {
            if (validData()) {
                createProfile()
            }
        }

        viewModel.insertProfileObserver.observe(this.viewLifecycleOwner) { profile ->
            if (profile != null) {
                adsUtils.interSaveProfile.showInterAdsBeforeNavigate(requireContext(), false) {
                    prefUtils.profile = profile
                    val action =
                        ProfileEditFragmentDirections.actionProfileEditFragmentToHomeFragment()
                    findNavController().navigate(action)
                }
            } else {
                toast(getString(R.string.cannot_add_profile))
            }
        }
    }

    private fun createProfile() {
        val valueName = binding.edtName.textTrim()
        val valueBirthYear = binding.sprYearOfBirth.intNumber()
        val valueGender = when (binding.sprGender.textSelected()) {
            getString(R.string.male) -> 1
            getString(R.string.female) -> 2
            else -> {
                0
            }
        }
        val valueWeight = binding.edtWeight.intNumber()
        val valueHeight = binding.edtHeight.intNumber()

        val newProfile = Profile(
            if (args.editMode) 0 else 1,
            valueName,
            valueBirthYear,
            valueGender,
            valueHeight,
            valueWeight,
        )

        viewModel.insertProfile(newProfile)
    }

    private fun validData(): Boolean {
        if (binding.edtName.isBlank() || binding.edtHeight.isBlank() || binding.edtWeight.isBlank()) {
            toast(getString(R.string.missing_required_information))
            return false
        }
        return true
    }
}