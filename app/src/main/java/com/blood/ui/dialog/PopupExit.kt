package com.blood.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.blood.App
import com.blood.utils.PrefUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BuildConfig
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.DialogExitBinding
import com.ironsource.mediationsdk.utils.IronSourceUtils.isNetworkConnected
import javax.inject.Inject

class PopupExit : DialogFragment() {

    private lateinit var binding: DialogExitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_exit, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            tvExit.clickWithDebounce {
                requireActivity().finish()
            }

            tvCancel.clickWithDebounce {
                dismiss()
            }
        }

        if (isNetworkConnected(requireContext()) && (PrefUtils.instant.isShowNativeExitHigh || PrefUtils.instant.isShowNativeExit)) {
            App.adsUtils.nativeLanguage.showAds(
                requireActivity(),
                BuildConfig.native_language_high,
                BuildConfig.native_language,
                null,
                R.layout.native_medium,
                binding.flAds,
                false,
                reloadAfterShow = true
            )
        } else {
            binding.flAds.visibility = View.GONE
        }
    }
}