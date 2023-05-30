package com.blood.ui.fragments.bloodpressure

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.blood.base.BaseFragment
import com.blood.data.InfoKnowledge
import com.blood.ui.dialog.YesNoPopup
import com.blood.ui.fragments.home.HomeFragment
import com.blood.utils.AdsUtils.BannerUtils.loadBanner
import com.blood.utils.ViewUtils.clickWithDebounce
import com.blood.utils.customview.HeaderView
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BR
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentBloodPressureDetailBinding

class BloodPressureDetailFragment :
    BaseFragment<BloodPressureViewModel, FragmentBloodPressureDetailBinding>(
        R.layout.fragment_blood_pressure_detail, BloodPressureViewModel::class.java
    ), HeaderView.Listener {

    val args: BloodPressureDetailFragmentArgs by navArgs()

    override fun init(inflater: LayoutInflater, container: ViewGroup) {
        super.init(inflater, container)
        binding.setVariable(BR.bloodPressureDetailFragment, this)
    }

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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBack()
                }
            })

        with(binding) {
            tvDisclaimer.clickWithDebounce {
                findNavController().navigate(BloodPressureDetailFragmentDirections.actionBloodPressureDetailFragmentToDisclaimerFragment())
            }

            btnHelp.clickWithDebounce {
                val action =
                    BloodPressureDetailFragmentDirections.actionBloodPressureDetailFragmentToInfoDetailFragment(
                        InfoKnowledge.getListBloodPressure()[5]
                    )
                findNavController().navigate(action)
            }
        }

        viewModel.deleteBloodObserver.observe(this.viewLifecycleOwner) { isDeleted ->
            if (isDeleted) {
                toast(getString(R.string.delete_blood_success_msg))
                onBack()
            }
        }
    }

    override fun onHeaderBackPressed() {
        onBack()
    }

    override fun onOptionPressed(view: View) {
        showOptionPopup(view)
    }

    private fun onBack() {
        if (HomeFragment::class.java.name.equals((findNavController().previousBackStackEntry?.destination as? FragmentNavigator.Destination)?.className)) {
            findNavController().popBackStack(
                R.id.homeFragment, inclusive = false, saveState = true
            )
        } else {
            findNavController().popBackStack(
                R.id.insightBloodPressureFragment, inclusive = false, saveState = true
            )
        }
    }

    private fun showOptionPopup(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.menu_popup_option_blood_detail)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.actionEdit -> {
                    val action =
                        BloodPressureDetailFragmentDirections.actionBloodPressureDetailFragmentToBloodPressureEditFragment()
                    action.modeAdd = false
                    action.mustShowBackButton = true
                    action.id = args.id
                    findNavController().navigate(action)
                    true
                }

                R.id.actionDelete -> {
                    YesNoPopup.showPopup(
                        childFragmentManager,
                        R.string.delete_result,
                        R.string.this_result_will_be_deleted_forever_are_you_sure
                    ) {
                        viewModel.deleteBloodById(args.id)
                    }
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