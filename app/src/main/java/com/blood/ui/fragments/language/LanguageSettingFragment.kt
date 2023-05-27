package com.blood.ui.fragments.language

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.common.Constant
import com.blood.ui.adapters.LanguageSettingAdapter
import com.blood.utils.LanguageUtils
import com.blood.utils.ViewUtils.clickWithDebounce
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentLanguageSettingBinding


class LanguageSettingFragment : BaseFragment<BaseViewModel, FragmentLanguageSettingBinding>(
    R.layout.fragment_language_setting, BaseViewModel::class.java
), LanguageSettingAdapter.Callback {

    override fun initData() {
        val languages = LanguageUtils.languageListItems(requireContext())
        for (language in languages) {
            if (language.code == prefUtils.defaultLanguage) {
                language.isChoose = true
            }
        }
        binding.rcvLanguage.adapter = LanguageSettingAdapter(languages, this)
    }

    override fun initListener() {
        super.initListener()
        binding.btnBack.clickWithDebounce {
            findNavController().navigateUp()
        }
    }

    override fun onLanguageClicked(languageCode: String) {
        if (languageCode != prefUtils.defaultLanguage) {
            prefUtils.defaultLanguage = languageCode
            LanguageUtils.changeLanguage(requireContext(), languageCode)

            val intent = requireActivity().intent
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            intent.putExtra(Constant.KEY_START_SPLASH_FROM, true)
            requireActivity().finish()
            startActivity(intent)
        }
    }
}