package com.blood.ui.fragments.language

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.blood.base.BaseFragment
import com.blood.base.BaseViewModel
import com.blood.base.recyclerview.BaseRecyclerViewListener
import com.blood.common.Constant
import com.blood.data.Language
import com.blood.utils.LanguageUtils
import com.blood.utils.customview.HeaderView
import com.bloodpressure.pressuremonitor.bloodpressuretracker.BR
import com.bloodpressure.pressuremonitor.bloodpressuretracker.R
import com.bloodpressure.pressuremonitor.bloodpressuretracker.databinding.FragmentLanguageSettingBinding


class LanguageSettingFragment : BaseFragment<BaseViewModel, FragmentLanguageSettingBinding>(
    R.layout.fragment_language_setting, BaseViewModel::class.java
), BaseRecyclerViewListener<Language>, HeaderView.Listener {

    override fun init(inflater: LayoutInflater, container: ViewGroup) {
        super.init(inflater, container)
        binding.setVariable(BR.languageSettingFragment, this)
    }

    override fun onHeaderBackPressed() {
        safeBackNav()
    }

    override fun onClick(data: Language, position: Int) {
        prefUtils.defaultLanguage = data.code
        LanguageUtils.changeLanguage(requireContext(), data.code)

        val intent = requireActivity().intent
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        intent.putExtra(Constant.KEY_START_SPLASH_FROM, true)
        requireActivity().finish()
        startActivity(intent)
    }
}