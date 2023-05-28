package com.blood.ui.fragments.bloodpressure

import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.blood.base.BaseFragment
import com.blood.utils.AdsUtils.BannerUtils.loadBanner
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentBloodPressureDetailBinding

class BloodPressureDetailFragment :
    BaseFragment<BloodPressureViewModel, FragmentBloodPressureDetailBinding>(
        R.layout.fragment_blood_pressure_detail, BloodPressureViewModel::class.java
    ) {

    val args: BloodPressureDetailFragmentArgs by navArgs()

    override fun initAds() {
        super.initAds()
        binding.flBanner.loadBanner(
            requireActivity(), BuildConfig.banner_home, prefUtils.isShowBannerHome
        )
    }

    override fun initData() {
        viewModel.getBloodPressureByID(args.id)
    }

    override fun initListener() {
        super.initListener()

        with(binding) {
            btnBack.clickWithDebounce {
                findNavController().navigateUp()
            }

            tvDisclaimer.clickWithDebounce {
                findNavController().navigate(BloodPressureDetailFragmentDirections.actionBloodPressureDetailFragmentToDisclaimerFragment())
            }

            ivOption.clickWithDebounce {
                showOptionPopup()
            }
        }
    }

    private fun showOptionPopup() {
        val popupMenu = PopupMenu(requireContext(), binding.ivOption)
        popupMenu.inflate(R.menu.menu_popup_option_blood_detail)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.actionEdit -> {
                    val action =
                        BloodPressureDetailFragmentDirections.actionBloodPressureDetailFragmentToBloodPressureEditFragment()
                    action.modeAdd = false
                    action.id = args.id
                    findNavController().navigate(action)
                    true
                }

                R.id.actionDelete -> {
//                            showDialog(record)
                    true
                }

                else -> false
            }
        }

        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            popupMenu.show()
        }
    }
}